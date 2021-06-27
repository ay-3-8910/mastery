package by.example.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sergey Tsynin
 */
@RestController
public class VersionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionController.class);

    private final static String VERSION = "version 1";

    @GetMapping(value = "/version")
    public String version() {
        LOGGER.debug("Version request");
        return VERSION;
    }
}