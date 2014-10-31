/**
 * Created by Brian on 10/18/2014.
 */
class RopeTest extends GroovyTestCase {
    def testRope
    def testString

    void setUp() {
        super.setUp()

        testRope = new Rope("The quick brown fox jumps over the lazy dog", 5, false)
        testString = "The quick brown fox jumps over the lazy dog"
    }

    void testLength() {
        assert testRope.length() == testString.length()
    }

    void testGetAt() {
        testString.eachWithIndex { character, i ->
            assert testRope[i] == character
        }
    }

    void testDestructiveConcatenate() {
        def rope1 = new Rope("The quick brown f", 5, true)
        def rope2 = new Rope("ox jumps over the lazy dog")
        def rope1Address = Integer.toHexString(System.identityHashCode(rope1))
        def concatenateRope = rope1 + rope2

        assert !rope1.is(concatenateRope)
        assert concatenateRope.toString() == testString
    }

    void testNonDestructiveConcatenate() {
        def rope1 = new Rope("The quick brown f")
        def rope2 = new Rope("ox jumps over the lazy dog")
        def originalRope1 = rope1
        rope1 += rope2

        assert originalRope1.is(rope1)
        assert rope1.toString() == testString
    }

    void testSplit() {
        Rope.split(testRope, 10)

    }

    void testRopeEquals() {
        def rope2 = new Rope(testString)

        assert testRope == rope2
    }

    void testStringEquals() {
        assert testRope.toString() == testString
    }

    void testGenerateString() {
        def output = testRope.toString()
        assert output == testString
    }

    def blah() {
        def foo = new Rope("Foo", 5, true)
    }
}
