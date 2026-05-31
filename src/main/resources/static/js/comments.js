(() => {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    document.querySelectorAll('.ajax-delete-comment').forEach((button) => {
        button.addEventListener('click', async () => {
            if (!confirm('Удалить комментарий?')) {
                return;
            }

            const headers = {
                'Accept': 'application/json'
            };
            if (csrfToken && csrfHeader) {
                headers[csrfHeader] = csrfToken;
            }

            button.disabled = true;
            try {
                const response = await fetch(button.dataset.deleteUrl, {
                    method: 'POST',
                    headers
                });

                if (!response.ok) {
                    throw new Error(`Delete comment request failed: ${response.status}`);
                }

                const data = await response.json();
                const card = document.querySelector(`#comment-${data.commentId}`);
                if (card) {
                    card.classList.add('deleted');
                    card.querySelector('.comment-content').textContent = 'Комментарий удалён';
                    button.remove();
                }
            } catch (error) {
                console.error(error);
                alert('Не удалось удалить комментарий. Попробуйте ещё раз.');
                button.disabled = false;
            }
        });
    });
})();
