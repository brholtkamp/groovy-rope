/**
 * Created by Brian on 9/22/2014.
 */

//TODO: Create a table of all characters in the left subtree
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

        // Delete the substrings incase the string is grossly large, prevents memory hits since it's already in the node form
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
            return leftChild.computeSubtree() + rightChild.computeSubtree() + weight
        } else if (leftChild && !rightChild) {
            return leftChild.computeSubtree() + weight
        } else if (!leftChild && rightChild) {
            return rightChild.computeSubtree() + weight
        } else {
            return weight
        }
    }
}