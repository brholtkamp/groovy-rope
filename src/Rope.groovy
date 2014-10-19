/**
 * Created by Brian on 9/22/2014.
 */

//TODO: Extend iterate (for characters)

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
        splitLength = inputSplitLength
        generateRope(inputString)
    }

    def size() {
        return root.weight
    }

    def getAt(def input) {
        return index(root, input)
    }

    def index(def node, def currentWeight) {
        if (node.weight <= currentWeight) {
            return index(node.rightChild, currentWeight - node.weight)
        } else {
            if (node.leftChild) {
                return index(node.leftChild, currentWeight)
            } else {
                return node.string[currentWeight]
            }
        }
    }

    def plus(def rope2) {
        return concatenate(this, rope2)
    }

    def concatenate(def rope1, def rope2) {
        def newNode = new RopeNode()
        newNode.leftChild = rope1.root.leftChild
        newNode.rightChild = rope2.root.leftChild

        def newRope = new Rope()
        def newRopeRoot = new RopeNode()
        newRopeRoot.leftChild = newNode
        newRope.root = newRopeRoot
        newRope.root.updateWeight()

        // Need to string the right leaf of rope1 to the left leaf of rope2 to get toString to be faster
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
                    newNode.leftChild.parent = newNode;
                }

                if (nodeList[0]) {
                    newNode.rightChild = nodeList[0]
                    nodeList.remove(nodeList.first())
                    newNode.rightChild.parent = newNode;
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


    @Override
    public String toString() {
        return generateString()
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