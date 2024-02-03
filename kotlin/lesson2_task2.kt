sealed interface Operation {
    fun calculate(): Double
}

class Value(val number: Double) : Operation {
    override fun calculate(): Double {
        return number
    }
}

class Multiply(val value1: Operation, val value2: Operation) : Operation {
    override fun calculate(): Double {
        return value1.calculate() * value2.calculate()
    }
}

class Plus(val value1: Operation, val value2: Operation) : Operation {
    override fun calculate(): Double {
        return value1.calculate() + value2.calculate()
    }
}


fun printOperationResult(operation: Operation) {
    val result = operation.calculate()
    println(result)
}

fun startTask2() {

    // Соответствует формуле 4 + 2.5 * 2
    printOperationResult(
        Plus(
            Value(4.0),
            Multiply(
                Value(2.5),
                Value(2.0)
            )
        )
    )

    // Соответствует формуле (1 + 3.5) * (2.5 + 2)
    printOperationResult(
        Multiply(
            Plus(
                Value(1.0),
                Value(3.5)
            ),
            Plus(
                Value(2.5),
                Value(2.0)
            )
        )
    )

    // Соответствует формуле 10
    printOperationResult(
        Value(10.0)
    )

}