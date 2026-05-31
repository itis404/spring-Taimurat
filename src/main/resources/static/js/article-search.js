(function () {
    const form = document.getElementById('article-filter-form');
    const list = document.getElementById('article-list');
    const status = document.getElementById('article-search-status');

    if (!form || !list || !status) {
        return;
    }

    const ajaxUrl = form.dataset.ajaxUrl;
    let timerId = null;

    form.addEventListener('submit', function (event) {
        event.preventDefault();
        runSearch();
        window.history.replaceState(null, '', form.action + '?' + new URLSearchParams(new FormData(form)).toString());
    });

    form.querySelectorAll('select, input[type="checkbox"]').forEach((element) => {
        element.addEventListener('change', runSearch);
    });

    const searchInput = form.querySelector('input[name="q"]');
    if (searchInput) {
        searchInput.addEventListener('input', function () {
            window.clearTimeout(timerId);
            timerId = window.setTimeout(runSearch, 350);
        });
    }

    function runSearch() {
        const params = new URLSearchParams(new FormData(form));
        status.textContent = 'Идёт поиск...';

        fetch(ajaxUrl + '?' + params.toString(), {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Search request failed');
                }
                return response.json();
            })
            .then((payload) => {
                renderArticles(payload.articles || []);
                status.textContent = 'Найдено: ' + payload.total;
            })
            .catch(() => {
                status.textContent = 'Ошибка поиска. Можно применить фильтры обычной отправкой формы.';
            });
    }

    function renderArticles(articles) {
        if (articles.length === 0) {
            list.innerHTML = '<div class="card empty-search-card"><h2>Ничего не найдено</h2><p>Попробуй изменить поисковый запрос, категорию, тег или сортировку.</p></div>';
            return;
        }

        list.innerHTML = articles.map(renderArticle).join('');
    }

    function renderArticle(article) {
        const tags = (article.tags || [])
            .map((tag) => '<span class="tag">' + escapeHtml(tag.name) + '</span>')
            .join('');

        return '' +
            '<article class="card article-card wide">' +
            '  <div class="article-meta">' +
            '    <a href="/categories/' + encodeURIComponent(article.categorySlug) + '">' + escapeHtml(article.categoryName) + '</a>' +
            '    <span>' + article.viewsCount + ' просмотров</span>' +
            '  </div>' +
            '  <h2><a href="/articles/' + encodeURIComponent(article.slug) + '">' + escapeHtml(article.title) + '</a></h2>' +
            '  <p>' + escapeHtml(article.summary) + '</p>' +
            '  <div class="tag-row">' + tags + '</div>' +
            '  <div class="article-footer">Автор: <a href="/authors/' + encodeURIComponent(article.authorUsername) + '">' + escapeHtml(article.authorDisplayName) + '</a></div>' +
            '</article>';
    }

    function escapeHtml(value) {
        if (value === null || value === undefined) {
            return '';
        }
        return String(value)
            .replaceAll('&', '&amp;')
            .replaceAll('<', '&lt;')
            .replaceAll('>', '&gt;')
            .replaceAll('"', '&quot;')
            .replaceAll("'", '&#039;');
    }
})();
