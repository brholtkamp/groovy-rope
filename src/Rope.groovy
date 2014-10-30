/**
 * Created by Brian on 9/22/2014.
 */

//TODO: Extend iterate (for characters)

class Rope {
    private def root
    private def splitLength
    private def destructiveConcat

    /**
     * Default constructor for a rope.
     * @param inputString Initial value of the rope
     * @param inputSplitLength Length of substrings within the rope
     * @param destructiveConcat Create new ropes upon concatenation if true, append to existing rope if false
     */
    Rope(def inputString = null, def inputSplitLength = 5, destructiveConcat = false) {
        splitLength = inputSplitLength
        generateRope(inputString)
    }

    // Operator overloads
    def getAt(def input) {
        return characterIndexOf(root, input)
    }

    def plus(def rope2) {
        return concatenate(this, rope2)
    }

    @Override
    boolean equals(def rope2) {
        return (this.toString() == rope2.toString())
    }

    boolean equals(String input) {
        return (this.toString() == input)
    }

    // Public Methods
    def length() {
        return root.weight
    }

    @Override
    String toString() {
        return generateString()
    }

    def split(def index, def input) {
        def newRope = new Rope()

        def splitNode = nodeIndexOf(root, input)



    }

    def insert(def index, def input) {
        
    }
    
    def delete(def startingIndex, def endingIndex) {
        
    }

    def report(def startingIndex, def endingIndex) {

    }

    // Private Methods

    private def characterIndexOf(def node, def currentWeight) {
        if (node.weight <= currentWeight) {
            return characterIndexOf(node.rightChild, currentWeight - node.weight)
        } else {
            if (node.leftChild) {
                return characterIndexOf(node.leftChild, currentWeight)
            } else {
                return node.string[currentWeight]
            }
        }
    }

    private def nodeIndexOf(def node, def currentWeight) {
        if (node.weight <= currentWeight) {
            return nodeIndexOf(node.rightChild, currentWeight - node.weight)
        } else {
            if (node.leftChild) {
                return nodeIndexOf(node.leftChild, currentWeight)
            } else {
                return node
            }
        }
    }


    //Todo:
    private static def concatenate(def rope1, def rope2) {
        def newNode = new RopeNode()

        // Combine the nodes to the left of the root of both ropes to concatenate
        newNode.leftChild = rope1.root.leftChild
        newNode.rightChild = rope2.root.leftChild

        // Create a new rope and hook in the concatenated node to the root
        def newRope = new Rope()
        def newRopeRoot = new RopeNode()
        newRopeRoot.leftChild = newNode
        newRope.root = newRopeRoot

        // Update the weights for this new rope
        newRope.root.updateWeight()

        // String together the right leaf of rope1 to the left leaf of rope2 to get toString to be faster
        def rope1Node = newRope.root.leftChild.leftChild
        def rope2Node = newRope.root.leftChild.rightChild

        while (rope1Node.leftChild || rope1Node.rightChild || rope2Node.leftChild || rope2Node.rightChild) {
            if (rope1Node.rightChild) {
                rope1Node = rope1Node.rightChild
            } else if (rope1Node.leftChild) {
                rope1Node = rope1Node.leftChild
            }

            if (rope2Node.leftChild) {
                rope2Node = rope2Node.leftChild
            } else if (rope2Node.rightChild) {
                rope2Node = rope2Node.rightChild
            }
        }

        rope1Node.next = rope2Node

        return newRope
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
        // Starts at 1 so it links 0 -> 1, ...
        for (i in 1..nodeList.size()) {
            nodeList[i-1].next = nodeList[i]
        }

        // Delete the substrings in case the string is grossly large, prevents memory hits since it's already in the node form
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
                    nodeList.remove(nodeList.first())
                }

                if (nodeList[0]) {
                    newNode.rightChild = nodeList[0]
                    nodeList.remove(nodeList.first())
                }

                newNodeList.add(newNode)
            }
            // Overwrite the list so we can continue working
            nodeList = newNodeList
        }

        // Add in the root node and append our tree to the left
        root = new RopeNode()
        root.leftChild = nodeList[0]

        // With the tree finalized, update the weights
        root.updateWeight()
    }

    private def generateString() {
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
}

class RopeNode {
    def string
    def weight
    def leftChild
    def rightChild
    def next

    RopeNode() {
        weight = 0
    }

    RopeNode(def inputString) {
        string = inputString
        weight = inputString.size()
    }

    def updateWeight() {
        // If we're a leaf, get our weight and return this
        if (string) {
            weight = string.size()
            return weight;
        } else { // We're a non-leaf node, so we add up our left subtree
            weight = leftChild.computeSubtree()
            if (leftChild) {
                leftChild.updateWeight();
            }

            if (rightChild) {
                rightChild.updateWeight();
            }
        }
    }

    def computeSubtree() {
        // Check to see if we have children, and request their subtrees
        if (leftChild && rightChild) {
            return leftChild.computeSubtree() + rightChild.computeSubtree()
        } else if (leftChild && !rightChild) {
            return leftChild.computeSubtree()
        } else if (!leftChild && rightChild) {
            return rightChild.computeSubtree()
        } else {
            return weight
        }
    }
}