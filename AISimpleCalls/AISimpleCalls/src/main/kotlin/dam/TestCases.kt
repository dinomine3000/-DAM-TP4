package dam

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("\n🤖 Starting LLM Assistant application... 😀😀😀😀😀\n")

    // Get configuration properties
    val properties = getProperties()

    // Set up logging
    configureLogging(properties)
    println()

    // Write LLM used
    println("✨ Using AI_LLM: ${properties.getProperty("AI_LLM")}")

    // Use the factory to create the appropriate assistant based on configuration
    val assistant: AIAssistantNvidiaClasses = AIAssistantNvidiaClasses(properties)
    println()

    // Write system and model
    println("✨ Using: ${assistant.getSystem()} ${assistant.model}\n")

    val testTemps = listOf(0.0, 0.5, 1.0)
    val prompt1 = "Give me 5 overwatch heroes"
    val prompt2 = "What is Lord of Mysteries?"
    testPrompt(assistant, testTemps, prompt1)

    //test prompt 2
    testPrompt(assistant, testTemps, prompt2)
}

private suspend fun testPrompt(agent: AIAssistantNvidiaClasses, temps: List<Double>, prompt: String){
    println("\nTesting prompt:\n$prompt")
    for(temp in temps){
        agent.newTemperature = temp
        val output = agent.processInput(prompt)
        println("\nTemperature $temp Answer: $output\n")
    }
}
