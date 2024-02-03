interface ValueChangeListener {
    fun onNewValue(number: Int)
}

class Holder private constructor(number: Int) {

    var number: Int = number
        set(value) {
            field = value
            listener?.onNewValue(value)
        }

    var listener: ValueChangeListener? = null

    companion object {
        const val DEFAULT_NUMBER = -1
        fun createHolder(defaultNumber: Int): Holder {
            return Holder(defaultNumber)
        }
    }
}


fun startTask1() {
    val holder = Holder.createHolder(Holder.DEFAULT_NUMBER)
    holder.number = 9
    holder.listener = object : ValueChangeListener {
        override fun onNewValue(number: Int) {
            println("New value is $number")
        }
    }
    holder.number = 1
}