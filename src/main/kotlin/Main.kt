package org.example


sealed interface A {
    data class B(val boolean: Boolean): A
    data class C(val int: Int): A
}

sealed interface Either<out A, out B>{
    data class Left<A>(val value: A): Either<A, Nothing>
    data class Right<B>(val value: B): Either<Nothing, B>
}

fun <A, B, C> Either<A, B>.flatMap(f:(B) -> Either<A,C>): Either<A, C> = when(this){
    is Either.Left -> this
    is Either.Right -> f(this.value)
}

fun <A, B, C> Either<A, B>.map(f:(B) -> C): Either<A, C> = when(this){
    is Either.Left -> this
    is Either.Right -> Either.Right(value = f(this.value))
}

fun <E, R> Either<E, R>.getOrElse(f:(E) -> R): R = when(this){
    is Either.Left -> f(this.value)
    is Either.Right -> value
}

fun <E, R, R1: R, R2: R> Either<E, R1>.OrElse(other: Either<E, R2>): Either<E, R> = when(this){
    is Either.Left -> other
    is Either.Right -> this
}

                                /*Сделано дома*/
fun <E, R1, R2> liftEither(f: (R1) -> R2): (Either<E, R1>) -> Either<E, R2> = { either ->
    when(either){
        is Either.Left -> either
        is Either.Right -> Either.Right(f(either.value))
    }
}

fun <E, R1, R2, R3> map2(t: Either<E, R1>, v: Either<E, R2>, f: (R1, R2) -> R3): Either<E, R3> = when(t) {
        is Either.Left -> t
        is Either.Right -> when(v){
            is Either.Left -> v
            is Either.Right -> Either.Right(f(t.value, v.value))
        }
}

fun <E, R1, R2> List<R1>.traverseEither(f: (R1) -> Either<E, R2>): Either<E, List<R2>> {
    val list = mutableListOf<R2>()
    for (elem in this){
        when (val listElem = f(elem)){
            is Either.Left -> return listElem
            is Either.Right -> list.add(listElem.value)
        }
    }
    return Either.Right(list)
}


/*fun sum(a: Int, b: Int): Int = { a, b -> a + b }
fun sumCurried(a: Int): (Int) -> Int = { b -> a + b }

val sum3 = sumCurried(3)
val sum5 = sumCurried(5)*/

fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C = {a -> {b -> f(a, b)}}
fun <A, B, C> uncurry(f: (A) -> (B) -> C): (A, B) -> C = {a, b -> f(a)(b)}
fun <A, B, C> partial(f: (A, B) -> C, a: A): (B) -> C = {b -> f(a, b)}




fun main() {
}