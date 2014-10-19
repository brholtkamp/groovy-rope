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
        for (int i = 0; i < testRope.size(); i++) {
            assertEquals(testRope[i], testString[i])
        }
    }

    void testAppend() {

    }

    void testInsert() {

    }

    void testGenerateString() {
        assertEquals(testRope.toString(), testString)
    }
}
