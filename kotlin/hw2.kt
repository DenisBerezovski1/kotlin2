import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Attribute
import kotlin.system.exitProcess

/*
KOTLIN - HW2
------------
0. За основу берём код решения домашнего задания из предыдущего семинара и дорабатываем его.

1. Создайте иерархию sealed классов, которые представляют собой команды. В корне иерархии интерфейс Command.

2. В каждом классе иерархии должна быть функция isValid(): Boolean, которая возвращает true, если команда введена с
корректными аргументами. Проверку телефона и email нужно перенести в эту функцию.

3. Напишите функцию readCommand(): Command, которая читает команду из текстового ввода, распознаёт её и
возвращает один из классов-наследников Command, соответствующий введённой команде.

4. Создайте data класс Person, который представляет собой запись о человеке.
Этот класс должен содержать поля:
● name – имя человека
● phone – номер телефона
● email – адрес электронной почты

5. Добавьте новую команду show, которая выводит последнее значение, введённой с помощью команды add. Для этого
значение должно быть сохранено в переменную типа Person. Если на момент выполнения команды show не было
ничего введено, нужно вывести на экран сообщение “Not initialized”.

6. Функция main должна выглядеть следующем образом. Для каждой команды от пользователя:
a. Читаем команду с помощью функции readCommand
b. Выводим на экран получившийся экземпляр Command
c. Если isValid для команды возвращает false, выводим help. Если true, обрабатываем команду внутри when.
*/

val HELP_MESSAGE: String = """
        Перечень команд:
        
              exit
                - прекращение работы
                
              help
                - справка
                
              add <Имя> phone <Номер телефона>
                - сохранение записи с введенными именем и номером телефона
                 
              add <Имя> email <Адрес электронной почты>
                - сохранение записи с введенными именем и адрес электронной почты
                
              show
                - выводит последнее значение, введённой с помощью команды add
              
    """.trimIndent()
val COMMON_ERROR_MESSAGE: String =
    Ansi.colorize("Ошибка! Команда введена неверно. Список команд ниже", Attribute.BRIGHT_RED_TEXT())

var user: Person? = null


sealed interface Command {
    fun execute()
    fun isValid(): Boolean
}


class ExitCommand : Command {

    override fun execute() {
        exitProcess(0)
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun toString(): String {
        return Ansi.colorize("Введена команда \"exit\"", Attribute.BRIGHT_GREEN_TEXT())
    }
}


class HelpCommand : Command {

    override fun execute() {
        println(HELP_MESSAGE)
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun toString(): String {
        return Ansi.colorize("Вывод справочной информации", Attribute.BRIGHT_GREEN_TEXT())
    }
}

class AddUserPhoneCommand(private val entryData: List<String>) : Command {

    private val phonePattern = Regex("[+]+\\d+")
    private val entryPhone = entryData[entryData.indexOf("phone") + 1]

    override fun execute() {
        user = Person(name = entryData[0], phone = entryPhone)
    }

    override fun isValid(): Boolean {
        return entryPhone.matches(phonePattern) && entryData.size <= 3
    }

    override fun toString(): String {
        return Ansi.colorize(
            "Введена команда записи нового пользователя ${entryData[0]} с номером телефона $entryPhone",
            Attribute.BRIGHT_GREEN_TEXT()
        )
    }
}

class AddUserEmailCommand(private val entryData: List<String>) : Command {

    private val emailPattern = Regex("[a-zA-z0-9]+@[a-zA-z0-9]+[.]([a-zA-z0-9]{2,4})")
    private val entryEmail = entryData[entryData.indexOf("email") + 1]

    override fun execute() {
        user = Person(name = entryData[0], email = entryEmail)
    }

    override fun isValid(): Boolean {
        return entryEmail.matches(emailPattern) && entryData.size <= 3
    }

    override fun toString(): String {
        return Ansi.colorize(
            "Введена команда записи нового пользователя ${entryData[0]} с адресом электронной почты $entryEmail",
            Attribute.BRIGHT_GREEN_TEXT()
        )
    }
}

class ShowCommand : Command {
    override fun execute() {
        if (user == null) {
            println("Not initialized")
        } else {
            println(user)
        }
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun toString(): String {
        return Ansi.colorize("Введена команда \"show\"", Attribute.BRIGHT_GREEN_TEXT())
    }

}

data class Person(
    var name: String,
    var phone: String? = null,
    var email: String? = null
) {
    override fun toString(): String {
        return "Пользователь ${this.name}: " + this.phone.orEmpty() + this.email.orEmpty()
    }
}


fun readCommand(): Command {
    print("> ")
    val entryData: List<String> = readln().lowercase().split(' ')

    return when (entryData[0]) {
        "add" -> {
            if (entryData.size > 3 && "phone" in entryData && "email" !in entryData) {
                AddUserPhoneCommand(entryData.subList(1, entryData.size))
            } else if (entryData.size > 3 && "phone" !in entryData && "email" in entryData) {
                AddUserEmailCommand(entryData.subList(1, entryData.size))
            } else {
                println(COMMON_ERROR_MESSAGE)
                HelpCommand()
            }
        }

        "show" -> ShowCommand()
        "help" -> HelpCommand()
        "exit" -> ExitCommand()
        else -> {
            println(COMMON_ERROR_MESSAGE)
            return HelpCommand()
        }
    }
}


fun hw2() {

    println("Введите команду или \"help\" для вывода списка команд ")

    while (true) {
        val command: Command = readCommand()
        if (command.isValid()) {
            println(command)
            command.execute()
        } else {
            println(COMMON_ERROR_MESSAGE)
            println(HELP_MESSAGE)
        }
    }
}
