/**
 * Created by Brian on 9/22/2014.
 */

//TODO: Extend iterate (for characters)
//TODO: Input verification for methods

class Rope {
    private def root
    private def splitLength
    private def destructiveConcatenate

    /**
     * Default constructor for a rope.
     * @param inputString Initial value of the rope
     * @param inputSplitLength Length of substrings within the rope
     * @param destructiveConcatenate Creates a new rope upon concatenation if true, append to existing rope if false
     */
    Rope(def inputString = null, def inputSplitLength = 5, inputDestructiveConcatenate = false) {
        splitLength = inputSplitLength
        if (inputString) {
            generateRope(inputString)
        }
        destructiveConcatenate = inputDestructiveConcatenate
    }

    // Operator overloads
    def getAt(def input) {
        return characterAtIndexOf(root, input)
    }

    def plus(def rope2) {
        return concatenate(this, rope2)
    }

    @Override
    boolean equals(def rope2) {
        return this.toString() == rope2.toString()
    }

    boolean equals(String input) {
        return this.toString() == input
    }

    // Public Methods
    def length() {
        return root.weight
    }

    @Override
    String toString() {
        return generateString()
    }

    /**
     *
     * @param inputRope
     * @param splitLocation Starts at index of 0
     * @return
     */
    def static split(def inputRope, def splitLocation) {
        def firstRope = new Rope()
        def firstRopeNodes = []
        def secondRope = new Rope()
        def secondRopeNodes = []

        // Determine where the splitting should occur
        def splitNode = nodeIndexOf(inputRope.root, splitLocation)
        def splittingCharacter = characterAtIndexOf(inputRope.root, splitLocation)
        def splittingIndex = indexOfCharacterOf(inputRope.root, splitLocation)

        // Iterate through to the leftmost leaf
        def currentNode = inputRope.root
        while (currentNode.leftChild) {
            currentNode = currentNode.leftChild
        }

        // Begin adding in the first tree's nodes until you reach the split point
        while (currentNode != splitNode) {
            firstRopeNodes.add(currentNode)
            currentNode = currentNode.next
        }

        // Unlink the two lists of nodes
        if (currentNode.previous) {
            if (currentNode.previous.next) {
                currentNode.previous.next = null
            }
        }
        if (currentNode.next) {
            if (currentNode.next.previous) {
                currentNode.next.previous = null
            }
        }

        // Determine if we should do a node split or not
        if (splittingIndex == splitNode.string.length() - 1) {
            // No node split necessary
            firstRopeNodes.add(currentNode)
            currentNode = splitNode.next
        } else {
            // Node split necessary
            def buffer = new StringBuffer()
            def firstRopeSubstring
            def secondRopeSubstring

            for (i in 0..splittingIndex) {
                buffer.append(splitNode.string[i])
            }
            firstRopeSubstring = buffer.toString()
            buffer.setLength(0)

            for (i in splittingIndex + 1..splitNode.string.length() - 1) {
                buffer.append(splitNode.string[i])
            }
            secondRopeSubstring = buffer.toString()

            // Add those nodes to the lists of nodes
            firstRopeNodes.add(new RopeNode(firstRopeSubstring))
            secondRopeNodes.add(new RopeNode(secondRopeSubstring))

            // Link the last node of the first rope into the previous nodes
            firstRopeNodes.last().previous = firstRopeNodes[firstRopeNodes.size() - 2]
            firstRopeNodes.last().previous.next = firstRopeNodes.last()

            // Move onto the next node in the list
            currentNode = currentNode.next
        }

        // Add on the second tree's nodes until the end
        while (currentNode) {
            secondRopeNodes.add(currentNode)
            currentNode = currentNode.next
        }

        // Link the first node to the rest of the second rope
        secondRopeNodes.first().next = secondRopeNodes[1]
        secondRopeNodes.first().next.previous = secondRopeNodes.first()

        // Generate ropes from the nodelists
        def output = []
        firstRope.root = new RopeNode()
        firstRope.root.leftChild = firstRope.combineNodes(firstRopeNodes)
        secondRope.root = new RopeNode()
        secondRope.root.leftChild = secondRope.combineNodes(secondRopeNodes)
        firstRope.root.updateWeight()
        secondRope.root.updateWeight()
        output.add(firstRope)
        output.add(secondRope)

        return output
    }

    def insert(def index, def input) {
        def insertRope = new Rope(input)

        def splitRopes = Rope.split(this, index)

        return splitRopes.first() + insertRope + splitRopes.last()
    }
    
    def delete(def startingIndex, def endingIndex) {
        def firstSplit = Rope.split(this, startingIndex)
        def secondSplit = Rope.split(firstSplit.last(), endingIndex - startingIndex)

        return firstSplit.first() + secondSplit.last()
    }

    def report(def startingIndex, def endingIndex) {
        def startingNode = nodeIndexOf(root, startingIndex)
        def endingNode = nodeIndexOf(root, endingIndex)
        def startingCharacterIndex = indexOfCharacterOf(root, startingIndex)
        def endingCharacterIndex = indexOfCharacterOf(root, endingIndex)

        def buffer = new StringBuffer()
        def currentNode = startingNode

        if (startingNode != endingNode) {
            // Get the characters from the startingNode
            for (i in startingCharacterIndex..currentNode.string.length() - 1) {
                buffer.append(currentNode.string[i])
            }
            currentNode = currentNode.next

            // Get the nodes inbetween
            while (currentNode != endingNode) {
                buffer.append(currentNode.string)
                currentNode = currentNode.next
            }

            for (i in 0..endingCharacterIndex) {
                buffer.append(currentNode.string[i])
            }
        } else {
            for (i in startingCharacterIndex..endingCharacterIndex) {
                buffer.append(currentNode.string[i])
            }
        }

        return buffer.toString()
    }

    // Private Methods
    private static def characterAtIndexOf(def node, def currentWeight) {
        if (node.weight <= currentWeight) {
            return characterAtIndexOf(node.rightChild, currentWeight - node.weight)
        } else {
            if (node.leftChild) {
                return characterAtIndexOf(node.leftChild, currentWeight)
            } else {
                return node.string[currentWeight]
            }
        }
    }

    private static def indexOfCharacterOf(def node, def currentWeight) {
        if (node.weight <= currentWeight) {
            return indexOfCharacterOf(node.leftChild, currentWeight - node.weight)
        } else {
            if (node.leftChild) {
                return indexOfCharacterOf(node.leftChild, currentWeight)
            } else {
                return currentWeight
            }
        }
    }

    private static def nodeIndexOf(def node, def currentWeight) {
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

    private def concatenate(def rope1, def rope2) {
        def newNode = new RopeNode()

        // Combine the nodes to the left of the root of both ropes to concatenate
        newNode.leftChild = rope1.root.leftChild
        newNode.rightChild = rope2.root.leftChild

        // Create a new rope and hook in the concatenated node to the root
        def newRope, newRopeRoot
        if (rope1.destructiveConcatenate) {
             newRope = new Rope()
             newRopeRoot = new RopeNode()
        } else {
            newRope = rope1
            newRopeRoot = new RopeNode()
        }
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
            if (i != nodeList.size()) {
                nodeList[i - 1].next.previous = nodeList[i - 1]
            }
        }

        // Delete the substrings in case the string is grossly large, prevents memory hits since it's already in the node form
        substrings.clear()

        // Add in the root node and append our tree to the left
        root = new RopeNode()
        root.leftChild = combineNodes(nodeList)

        // With the tree finalized, update the weights
        root.updateWeight()
    }

    private def combineNodes(def nodeList) {
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

        return nodeList[0]
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
    def previous

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
            return weight
        } else { // We're a non-leaf node, so we add up our left subtree
            weight = leftChild.computeSubtree()
            if (leftChild) {
                leftChild.updateWeight()
            }

            if (rightChild) {
                rightChild.updateWeight()
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