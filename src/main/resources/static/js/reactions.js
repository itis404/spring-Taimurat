(() => {
    const panel = document.querySelector('.reaction-panel[data-reaction-url]');
    if (!panel) {
        return;
    }

    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');
    const buttons = panel.querySelectorAll('.reaction-button');
    const likesCount = document.getElementById('likes-count');
    const dislikesCount = document.getElementById('dislikes-count');

    buttons.forEach((button) => {
        button.addEventListener('click', async () => {
            const type = button.dataset.reactionType;
            const body = new URLSearchParams();
            body.set('type', type);

            const headers = {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                'Accept': 'application/json'
            };

            if (csrfToken && csrfHeader) {
                headers[csrfHeader] = csrfToken;
            }

            button.disabled = true;
            try {
                const response = await fetch(panel.dataset.reactionUrl, {
                    method: 'POST',
                    headers,
                    body
                });

                if (!response.ok) {
                    throw new Error(`Reaction request failed: ${response.status}`);
                }

                const data = await response.json();
                likesCount.textContent = data.likes;
                dislikesCount.textContent = data.dislikes;

                buttons.forEach((item) => item.classList.remove('active'));
                if (data.currentUserReaction) {
                    const activeButton = panel.querySelector(`[data-reaction-type="${data.currentUserReaction}"]`);
                    activeButton?.classList.add('active');
                }
            } catch (error) {
                console.error(error);
                alert('Не удалось обновить реакцию. Попробуйте ещё раз.');
            } finally {
                button.disabled = false;
            }
        });
    });
})();
