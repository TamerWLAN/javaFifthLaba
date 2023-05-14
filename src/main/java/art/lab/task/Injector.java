package art.lab.task;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class Injector {
    private String fileName;

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public final @NotNull Properties readPropertiesFile() {
        if (fileName.equals("")) {
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
