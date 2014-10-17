/**
 * Created by Brian on 9/22/2014.
 */
class Test {
    public static def main(def args) {
        println("Hello World!")
        def rope = new Rope("TheQuickBrown")
        println rope.toString()
        rope = new Rope("The Quick Brown Fox Jumped Over The Lazy Dog", 7)
        println rope.toString()
    }
}
