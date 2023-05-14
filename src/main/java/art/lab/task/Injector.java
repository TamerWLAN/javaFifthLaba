package art.lab.task;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.Properties;

/**
 * Класс инъектор, для инициализаци поле ЛЮБОГО класса
 * с аннотацией AutoInjectable
 * с помощью файла .properties
 * @author Artem Kozlitin
 * @version 1.5
 * @since 1.0
 */
public class Injector {
    /**
     * Имя и путь текущего файла .properties
     */
    private String fileName;

    /**
     * Метод, устанавливающий новое имя файла .properties
     * @param fileName
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Метод, читающий текущий файл .properties
     * и создающий объект Properties
     * @throws RuntimeException если имя файла не установлено
     * @return объект Properties на основе файла
     */
    private final @NotNull Properties readPropertiesFile() {
        if (fileName == null) {
            throw new RuntimeException("File path must be set");
        }

        final Properties properties = new Properties();

        try {
            final Reader input = new FileReader(fileName);
            final BufferedReader reader = new BufferedReader(input);
            properties.load(reader);
        } catch (final Exception e) {
            System.err.println(e.getMessage());
        }

        return properties;
    }

    /**
     * Метод инициализирующий поля класса, с помощью рефлексии, на основе содержимого .properties
     * печатающий ошибки в случае возникновения
     * @param object объект, у коготорого инициализируются поля
     * @return объект, с инициализированными полями
     * @param <T> класс, подлежащий инициализации (поля которого AutoInjectable)
     */
    public <T> T inject(T object) {
        Class<?> objectClass = object.getClass();

        final var properties = readPropertiesFile();

        for (var declaredField : objectClass.getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(AutoInjectable.class)) {
                final var fieldTypeName = declaredField.getType().getName();
                final var fieldInjectionClassName = properties.getProperty(fieldTypeName);


                try {
                    final Class<?> injectionClass = Class.forName(fieldInjectionClassName);
                    final Constructor<?> constructorInjectionClass = injectionClass.getDeclaredConstructor();

                    constructorInjectionClass.setAccessible(true);
                    final Object injectionObj = constructorInjectionClass.newInstance();
                    constructorInjectionClass.setAccessible(false);

                    declaredField.setAccessible(true);
                    declaredField.set(object, injectionObj);
                    declaredField.setAccessible(false);

                } catch (final Exception e) {
                    System.err.println(e.getMessage());
                }

            }
        }

        return object;
    }
}
