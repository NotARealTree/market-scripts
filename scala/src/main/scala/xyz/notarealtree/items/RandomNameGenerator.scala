package xyz.notarealtree.items

import scala.util.Random

/**
  * Created by Francis on 17/07/2016.
  */
class RandomNameGenerator {
    //TODO: Should probably load this from a file because wtf
    private val adverb: List[String] = List(
        "Astonishingly",
        "Shockingly",
        "Interestingly",
        "Fascinatingly",
        "Preposterously",
        "Hauntingly",
        "Beautifully",
        "Especially",
        "Timidly",
        "Sensuously",
        "Glaringly",
        "Handsomely",
        "Clumsily",
        "Daintily",
        "Dangerously",
        "Boldly",
        "Measurably",
        "Obnoxiously",
        "Ominously",
        "Patiently",
        "Questionably",
        "Scientifically",
        "Readily",
        "Suspiciously",
        "Tremendously",
        "Zealously",
        "Vainly",
        "Swiftly"
    )

    private val adjective: List[String] = List(
        "adventurous",
        "admirable",
        "beautiful",
        "bouncy",
        "cheap",
        "complex",
        "dangerous",
        "dishonest",
        "elementary",
        "fabulous",
        "gregarious",
        "haunting",
        "humongous",
        "jaded",
        "lame",
        "loathsome",
        "miserable",
        "offensive",
        "radiant",
        "rundown",
        "salty",
        "subtle",
        "tired",
        "unlucky",
        "violent",
        "wealthy",
        "wild",
        "virtuous"
    )

    private val noun: List[String] = List(
        "Whale",
        "Octopus",
        "Bear",
        "Shark",
        "Alpaca",
        "Elephant",
        "Lion",
        "Gazelle",
        "Boar",
        "Eagle",
        "Snake",
        "Fox",
        "Panda",
        "Monkey",
        "Owl",
        "Cat",
        "Horse",
        "Seal",
        "Crab",
        "Squirrel",
        "Turtle",
        "Fish",
        "Hawk",
        "Giraffe",
        "Seagull",
        "Wolf"
    )

    def generateRandomName(): String = {
        Random.shuffle(adverb).head + " " + Random.shuffle(adjective).head + " " + Random.shuffle(noun).head
    }

}