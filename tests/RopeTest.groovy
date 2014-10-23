/**
 * Created by Brian on 10/18/2014.
 */
class RopeTest extends GroovyTestCase {
    def testRope
    def testString

    void setUp() {
        super.setUp()

        testRope = new Rope("The quick brown fox jumps over the lazy dog")
        testString = "The quick brown fox jumps over the lazy dog"
    }

    void testSize() {
        assertEquals(testRope.size(), testString.size())
    }

    void testGetAt() {
        testString.eachWithIndex { character, i ->
            assertEquals(testRope[i], character)
        }
    }

    void testConcenate() {
        def rope1 = new Rope("The quick brown f")
        def rope2 = new Rope("ox jumps over the lazy dog")
        def concatenateRope = rope1 + rope2

        assertEquals(concatenateRope.toString(), testRope.toString())
    }

    void testRopeEquals() {
        def rope2 = new Rope(testString)

        assertTrue(testRope == rope2)
    }

    void testStringEquals() {
        assertTrue(testRope.toString() == testString)
    }

    void testGenerateString() {
        def output = testRope.toString()
        assertEquals(output, testString)
    }
}
