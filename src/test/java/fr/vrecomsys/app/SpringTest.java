package fr.vrecomsys.app;

import fr.vrecomsys.app.VrecomsysApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = VrecomsysApplication.class)
@ContextConfiguration
public @interface SpringTest {
}
