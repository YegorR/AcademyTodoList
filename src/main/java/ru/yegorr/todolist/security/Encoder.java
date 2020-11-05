package ru.yegorr.todolist.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Делает хэши паролей
 */
@Component
public class Encoder {
    private final PasswordEncoder encoder;

    /**
     * Конструктор
     */
    public Encoder() {
        encoder = new BCryptPasswordEncoder();
    }

    /**
     * Создаёт строку-хэш пароля
     *
     * @param password пароль
     * @return хэш
     */
    public String encryptPassword(String password) {
        return encoder.encode(password);
    }

    /**
     * Проверят пароль на подлинность по хэшу
     *
     * @param rawPassword "сырой" пароль
     * @param hash "хэш"
     * @return true, если пароль подлинный, иначе false
     */
    public boolean check(String rawPassword, String hash) {
        return encoder.matches(rawPassword, hash);
    }
}
