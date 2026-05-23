package dam

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("\n🤖 Starting Sentiment Analysis Test... \n")

    // Get configuration properties
    val properties = getProperties()

    // Set up logging
    configureLogging(properties)
    println()

    // Write LLM used
    println("✨ Using AI_LLM: ${properties.getProperty("AI_LLM")}")

    // Use the factory to create the appropriate assistant based on configuration
    // (We use AIAssistantNvidiaClasses here to mirror TestCases.kt, adjust if needed)
    val assistant = AIAssistantNvidiaClasses(properties)
    println()

    // Write system and model
    println("✨ Using: ${assistant.getSystem()} ${assistant.model}\n")

    val testInputs = listOf(
        "I absolutely love this new feature! It makes my life so much easier.",
        "The application crashed immediately after I opened it. Very disappointed.",
        "It's okay, I guess. Nothing special but it works."
    )

    for (input in testInputs) {
        testSentiment(assistant, input)
    }
}

private suspend fun testSentiment(agent: AIAssistant, input: String){
    println("\nEvaluating sentiment for input:\n\"$input\"")
    val output = agent.processSentimentInput(input)
    println("Response:\n$output\n")
}
