/**
 * Created by Brian on 9/22/2014.
 */

//TODO: Create a table of all characters in the left subtree
//TODO: Make it generic and push to Github
//TODO: Extend iterable (for characters)

class Rope {
    private def root
    private def splitLength = 5

    Rope() {
        root = null
    }

    Rope(def inputString) {
        generateRope(inputString)
    }

    Rope(def inputString, def inputSplitLength) {
        this.splitLength = inputSplitLength
        generateRope(inputString)
    }

    def append(def character) {

    }

    def insert(def index, def string) {

    }

    private def splitString(def inputString) {
        def substrings = []
        def substringBuffer = new StringBuilder()

        inputString.each {
            substringBuffer.append(it)
            if (substringBuffer.length() == splitLength) {
                substrings.add(substringBuffer.toString())
                substringBuffer.length = 0 // Reset the builder
            }
        }

        // Check to see if the buffer is cleared for the last string
        if (substringBuffer.length() > 0) {
            substrings.add(substringBuffer.toString())
        }

        return substrings
    }

    private def generateRope(def inputString) {
        // Split the string into the respective substrings
        def substrings = splitString(inputString)

        // Generate nodes for each substring
        def nodeList = []
        substrings.each {
            def node = new RopeNode(it)
            nodeList.add(node)
        }

        // String together the nodes to make string formation quicker
        for (i in 1..nodeList.size()) {
            nodeList[i-1].next = nodeList[i]
        }

        // Preemptively delete the substrings incase the string is grossly large, prevents memory hits since it's already in the node form
        substrings.clear()

        // Pair up and reduce the nodes until we're left with 1
        while (nodeList.size() != 1) {
            // Create a new list to hold the combined trees
            def newNodeList = []
            // Consume all of the list
            while (nodeList.size() > 0) {
                def newNode = new RopeNode()

                if (nodeList[0]) {
                    newNode.leftChild = nodeList[0]
                    nodeList[0].parent = newNode
                    nodeList.remove(0)
                }

                if (nodeList[0]) {
                    newNode.rightChild = nodeList[0]
                    nodeList[0].parent = newNode
                    nodeList.remove(0)
                }

                newNodeList.add(newNode)
            }
            // Overwrite the list so we can continue working
            nodeList = newNodeList
        }

        // Add in the root node and append our tree to the left
        this.root = new RopeNode()
        this.root.leftChild = nodeList[0]

        // With the tree finalized, update the weights
        this.root.updateWeight()
    }

    def generateString() {
        def currentNode = root
        def stringOutput = new StringBuffer()

        // Traverse to the left of the tree
        while (currentNode.leftChild) {
            currentNode = currentNode.leftChild
        }

        // Begin traversing to the right and adding up the strings
        while (currentNode) {
            stringOutput.append(currentNode.string)
            currentNode = currentNode.next
        }

        return stringOutput.toString()
    }


    @Override
    public String toString() {
        return this.generateString();
    }
}

class RopeNode {
    def string
    def weight
    def leftChild
    def rightChild
    def parent
    def next

    RopeNode() {
        weight = 0
    }

    RopeNode(def inputString) {
        this.string = inputString
        this.weight = inputString.size()
    }

    def updateWeight() {
        // If we're a leaf, get our weight and return this
        if (this.string) {
            this.weight = this.string.size()
            return this.weight;
        } else { // We're a concat node, so we add up our left subtree
            this.weight = this.leftChild.computeSubtree()
            if (this.leftChild) {
                this.leftChild.updateWeight();
            }

            if (this.rightChild) {
                this.rightChild.updateWeight();
            }
       }
    }

    def computeSubtree() {
        // Check to see if we have children, and request their subtrees
        if (this.leftChild && this.rightChild) {
            return this.leftChild.computeSubtree() + this.rightChild.computeSubtree() + this.weight
        } else if (this.leftChild && !this.rightChild) {
            return this.leftChild.computeSubtree() + this.weight
        } else if (!this.leftChild && this.rightChild) {
            return this.rightChild.computeSubtree() + this.weight
        } else {
            return this.weight
        }
    }
}