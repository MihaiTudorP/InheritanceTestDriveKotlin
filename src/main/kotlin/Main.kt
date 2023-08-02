import java.time.Instant
import java.util.Objects
import kotlin.random.Random

fun main(args: Array<String>) {
    val myMercedes = Car("Mercedes", "Blue", CarDriver("Louis", "Vettel"), 1, 5)
    val myCessna = Plane("Cessna", "White", Pilot("Amelia", "Earhart"), "piston", 2, 1)
    myMercedes.move()
    myMercedes.stop()
    myCessna.move()
    myCessna.stop()

    println("${myMercedes.driver.firstName} equals ${myCessna.driver.firstName}: ${myMercedes.driver == myCessna.driver}")
    val pilot2 = Pilot("Amelia", "Earhart")
    println("${pilot2.firstName} equals ${myCessna.driver.firstName}: ${pilot2 == myCessna.driver}")
    val boeing = PlaneBrand("Boeing", "Commercial")
    println("$boeing")
    val cessna = PlaneBrand("Cessna", "General Aviation")
    println("$cessna")
    println("${cessna.name} == ${boeing.name}: ${cessna == boeing}")
    val cessna2 = PlaneBrand("Cessna", "General Aviation")
    println("${cessna.name} == ${cessna2.name}: ${cessna == cessna2}")

    val edelbrock = object : Engine{
        override fun startEngine(){
            "The engine is starting with supercharger."
        }
    }

    edelbrock.startEngine()
}

fun getData(result: Result) {
    when (result) {
        is Result.Error -> {
            println("Error!")
            result.showMessage()
        }

        is Result.Success -> {
            println("Success!")
            result.showMessage()
        }
    }
}

sealed class Result(val message: String) {
    fun showMessage() {
        println("Result: $message")
    }

    class Success(message: String) : Result(message)
    class Error(message: String) : Result(message)
}

open class Vehicle(val name: String, val color: String, val driver: Driver) {
    open fun move() {
        driver.drive()
        println("$name is moving")
        getData(Result.Success("Successfully started"))
    }

    open fun stop() {
        println("$name is stopping...")
        val m = Random(100 + Instant.now().epochSecond).nextInt() * 1000 % 15
        if (m == 0) getData(Result.Error("Could not perform the operation"))
        else getData(Result.Success("Successfully stopped"))
    }
}

class Car(name: String, color: String, driver: CarDriver, val engines: Int, val doors: Int) :
    Vehicle(name, color, driver), Engine {
    override fun startEngine() {
        println("Engine started")
    }

    override fun move() {
        startEngine()
        super.move()
    }
}

class Plane(name: String, color: String, pilot: Pilot, val engineType: String, val wings: Int, val mandatoryCrew: Int) :
    Vehicle(name, color, pilot) {
    override fun move() {
        super.move()
        flying()
    }

    override fun stop() {
        landing()
        super.stop()
    }

    fun flying() {
        println("$name is flying")
    }

    fun landing() {
        println("$name is landing")
    }
}

abstract class Driver(val firstName: String, val lastName: String) {
    abstract fun drive()
}

class Pilot(firstName: String, lastName: String) : Driver(firstName, lastName) {
    override fun drive() {
        println("$firstName $lastName is at the controls")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is Pilot)
            return this.firstName === other.firstName && this.lastName === other.lastName
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(firstName, lastName)
    }


}

class CarDriver(firstName: String, lastName: String) : Driver(firstName, lastName) {
    override fun drive() {
        println("$firstName $lastName is behind the wheel")
    }
}

data class PlaneBrand(val name: String, val type: String)

interface Engine{
    fun startEngine()
}

// supports delegation using the by keyword