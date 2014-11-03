package com.brholtkamp

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
        def firstString = "The quick brown f"
        def secondString = "ox jumps over the lazy dog"

        // - 1 since it splits on index starting at 0
        def output = Rope.split(testRope, firstString.length() - 1)

        assert output.first() == firstString
        assert output.last() == secondString
        assert testRope == testString
    }

    void testInsert() {
        def insertString = "foo "
        def finalString = "The quick brown foo jumps over the lazy dog"
        def initialString = "The quick brown jumps over the lazy dog"
        def initialRope = new Rope(initialString)
        def initialRopeAddress = initialRope

        assert initialRope.insert(15, insertString) == finalString
        assert initialRope.is(initialRopeAddress) // Check for object destruction
    }

    void testDelete() {
        def initialString = "The quick brown ffffox jumps over the lazy dog"
        def initialRope = new Rope(initialString)
        def initialRopeAddress = initialRope
        initialRope.delete(16, 18)

        assert initialRope == testString
        assert initialRope.is(initialRopeAddress) // Check for object destruction
    }

    void testReport() {
        assert testRope.report(16, 18) == 'fox'
        assert testRope.report(1, 41) == 'he quick brown fox jumps over the lazy do'
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
}
