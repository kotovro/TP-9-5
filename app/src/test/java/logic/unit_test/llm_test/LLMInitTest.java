//package logic.integration_test.llm_test;
//
//import logic.protocol.LLMWrapper;
//import org.junit.jupiter.api.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class LLMInitTest {
//
//    private static LLMWrapper llmWrapper;
//
//    @BeforeAll
//    static void setUp() {
//        llmWrapper = new LLMWrapper();
//    }
//
//    @AfterAll
//    static void tearDown() {
//        if (llmWrapper.isInit()) {
//            llmWrapper.freeResources();
//        }
//    }
//
//    @Test
//    @Order(1)
//    void testLLMInitialization() {
//        assertFalse(llmWrapper.isInit(), "LLM should not be initialized initially");
//
//        llmWrapper.init();
//        assertTrue(llmWrapper.isInit(), "LLM should be initialized after init() call");
//    }
//
//  }