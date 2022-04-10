package org.cyb.tdd;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArgsTest {
    @Test
    @Disabled
    void should_parse_multi_options(){
        MultiOptions options = Args.parse(MultiOptions.class, "-l", "-p", "8080", "-d", "/usr/logs");
        assertTrue(options.logging);
        assertEquals(8080, options.port);
        assertEquals("/usr/logs", options.directory);
    }

    record MultiOptions(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory) {
    }

    @Test
    public void should_throw_illegal_option_exception_if_annotation_not_present() {
        IllegalOptionException e = assertThrows(IllegalOptionException.class,
                () -> Args.parse(OptionsWithoutAnnotation.class, "-l", "-p", "8080", "-d", "/usr/logs"));
        assertEquals("port", e.getValue());
    }

    record OptionsWithoutAnnotation(@Option("l")boolean logging, int port, @Option("d")String directory) {

    }

    @Test
    public void should_example_2() {
        ListOptions options = Args.parse(ListOptions.class, "-g", "this", "is", "a", "list", "-d", "1", "2", "-3", "5");
        assertArrayEquals(new String[]{"this", "is", "a", "list"}, options.group);
        assertArrayEquals(new Integer[]{1, 2, -3, 5}, options.decimals);
    }

    record ListOptions(@Option("g") String[] group, @Option("d") Integer[] decimals) {
    }
}
