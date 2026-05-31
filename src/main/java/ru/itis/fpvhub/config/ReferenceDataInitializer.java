package ru.itis.fpvhub.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.fpvhub.entity.ArticleEntity;
import ru.itis.fpvhub.entity.CategoryEntity;
import ru.itis.fpvhub.entity.ProfileEntity;
import ru.itis.fpvhub.entity.RoleEntity;
import ru.itis.fpvhub.entity.TagEntity;
import ru.itis.fpvhub.entity.UserEntity;
import ru.itis.fpvhub.entity.enums.ArticleStatus;
import ru.itis.fpvhub.entity.enums.RoleName;
import ru.itis.fpvhub.repository.ArticleRepository;
import ru.itis.fpvhub.repository.CategoryRepository;
import ru.itis.fpvhub.repository.RoleRepository;
import ru.itis.fpvhub.repository.TagRepository;
import ru.itis.fpvhub.repository.UserRepository;
import ru.itis.fpvhub.util.SlugGenerator;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
public class ReferenceDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ReferenceDataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    public ReferenceDataInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository,
            ArticleRepository articleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        initRolesAndUsers();
        initCategoriesAndTags();
        initArticles();
    }

    private void initRolesAndUsers() {
        Map<RoleName, String> roles = Map.of(
                RoleName.ROLE_USER, "Пользователь",
                RoleName.ROLE_WRITER, "Автор",
                RoleName.ROLE_ADMIN, "Администратор"
        );

        roles.forEach((roleName, displayName) -> roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new RoleEntity(roleName, displayName))));

        createUserIfMissing(
                "admin",
                "admin@fpvhub.local",
                "admin12345",
                "FPVHub Admin",
                RoleName.ROLE_USER,
                RoleName.ROLE_WRITER,
                RoleName.ROLE_ADMIN
        );
        createUserIfMissing(
                "writer",
                "writer@fpvhub.local",
                "writer12345",
                "Demo Writer",
                RoleName.ROLE_USER,
                RoleName.ROLE_WRITER
        );
        createUserIfMissing(
                "user",
                "user@fpvhub.local",
                "user12345",
                "Demo User",
                RoleName.ROLE_USER
        );
    }

    private void createUserIfMissing(
            String username,
            String email,
            String password,
            String displayName,
            RoleName... roles
    ) {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            return;
        }

        UserEntity user = new UserEntity(username, email, passwordEncoder.encode(password));
        user.verifyEmail();
        user.attachProfile(new ProfileEntity(displayName));

        for (RoleName roleName : roles) {
            RoleEntity role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalStateException("Role is not initialized: " + roleName));
            user.addRole(role);
        }

        userRepository.save(user);
        log.info("Created demo user: {} / {}", username, password);
    }

    private void initCategoriesAndTags() {
        createCategoryIfMissing("Сборка", "sborka", "Рамы, моторы, ESC, полётные контроллеры и компоновка квадрокоптера.");
        createCategoryIfMissing("Betaflight", "betaflight", "Настройка прошивки, портов, режимов, PID, rates и фильтров.");
        createCategoryIfMissing("ELRS", "elrs", "Радиосвязь ExpressLRS, привязка, частоты, мощность и failsafe.");
        createCategoryIfMissing("Видеосвязь", "video", "Камеры, VTX, антенны, частоты, качество картинки и задержка.");
        createCategoryIfMissing("Аккумуляторы", "batteries", "LiPo, зарядка, хранение, просадка напряжения и безопасность.");
        createCategoryIfMissing("Пилотирование", "piloting", "Симуляторы, режимы полёта, базовые упражнения и техника безопасности.");

        createTagIfMissing("5-inch", "5-inch");
        createTagIfMissing("cinewhoop", "cinewhoop");
        createTagIfMissing("betaflight", "betaflight");
        createTagIfMissing("elrs", "elrs");
        createTagIfMissing("vtx", "vtx");
        createTagIfMissing("lipo", "lipo");
        createTagIfMissing("pid", "pid");
        createTagIfMissing("safety", "safety");
    }

    private void createCategoryIfMissing(String name, String slug, String description) {
        categoryRepository.findBySlug(slug)
                .orElseGet(() -> categoryRepository.save(new CategoryEntity(name, slug, description)));
    }

    private void createTagIfMissing(String name, String slug) {
        tagRepository.findBySlug(slug)
                .orElseGet(() -> tagRepository.save(new TagEntity(name, slug)));
    }

    private void initArticles() {
        if (articleRepository.count() > 0) {
            return;
        }

        UserEntity writer = userRepository.findByUsernameIgnoreCase("writer")
                .orElseThrow(() -> new IllegalStateException("Demo writer is not initialized"));

        createPublishedArticle(
                writer,
                "С чего начать сборку первого FPV-квадрокоптера",
                "Базовый разбор компонентов: рама, моторы, ESC, полётный контроллер, приёмник, VTX, камера и аккумулятор.",
                "Первую сборку лучше начинать не с покупки самого мощного набора компонентов, а с понимания назначения каждого узла. Рама задаёт размер и прочность, моторы и пропеллеры формируют тягу, ESC управляет фазами моторов, полётный контроллер стабилизирует квадрокоптер, а видеосистема отвечает за картинку в очках. Для учебного 5-дюймового дрона важно оставить запас по току ESC, подобрать аккумулятор под вес и не экономить на пайке, изоляции и проверке короткого замыкания перед первым включением.",
                "sborka",
                Set.of("5-inch", "safety")
        );

        createPublishedArticle(
                writer,
                "Минимальная настройка Betaflight после сборки",
                "Что проверить в Betaflight Configurator перед первым arm: порты, приёмник, моторы, режимы, failsafe и OSD.",
                "После сборки нельзя сразу ставить пропеллеры и пытаться взлететь. Сначала нужно проверить ориентацию полётного контроллера, соответствие моторов схеме, направление вращения, работу приёмника, режим arm, beeper и failsafe. Отдельно стоит проверить вкладку Receiver: каналы должны двигаться правильно, центр стиков должен быть около 1500, а минимумы и максимумы близко к 1000 и 2000. Только после этого можно переходить к тесту моторов без пропеллеров и аккуратному первому взлёту.",
                "betaflight",
                Set.of("betaflight", "pid", "safety")
        );

        createPublishedArticle(
                writer,
                "ELRS: что проверить перед первым дальним полётом",
                "Короткий чеклист по ExpressLRS: bind phrase, model match, packet rate, мощность, телеметрия и failsafe.",
                "ExpressLRS даёт отличную дальность, но только при корректной настройке. Перед полётом нужно убедиться, что передатчик и приёмник используют совместимые версии, bind phrase совпадает, выбран разумный packet rate, а мощность не выставлена бездумно на максимум. Для безопасности обязательно проверяется failsafe: при выключении аппаратуры дрон должен уходить в безопасное состояние, а не продолжать выполнять последнюю команду. Также полезно вывести LQ и RSSI dBm в OSD, чтобы видеть качество связи в реальном времени.",
                "elrs",
                Set.of("elrs", "safety")
        );
    }

    private void createPublishedArticle(
            UserEntity author,
            String title,
            String summary,
            String content,
            String categorySlug,
            Set<String> tagSlugs
    ) {
        CategoryEntity category = categoryRepository.findBySlug(categorySlug)
                .orElseThrow(() -> new IllegalStateException("Category not found: " + categorySlug));

        Set<TagEntity> tags = new LinkedHashSet<>();
        for (String tagSlug : tagSlugs) {
            tags.add(tagRepository.findBySlug(tagSlug)
                    .orElseThrow(() -> new IllegalStateException("Tag not found: " + tagSlug)));
        }

        ArticleEntity article = new ArticleEntity(
                title,
                uniqueSeedSlug(title),
                summary,
                content,
                null,
                null,
                ArticleStatus.PUBLISHED,
                author,
                category,
                tags
        );
        article.publish();
        articleRepository.save(article);
    }

    private String uniqueSeedSlug(String title) {
        String base = SlugGenerator.fromTitle(title);
        String candidate = base;
        int index = 2;
        while (articleRepository.existsBySlug(candidate)) {
            candidate = base + "-" + index;
            index++;
        }
        return candidate;
    }
}
