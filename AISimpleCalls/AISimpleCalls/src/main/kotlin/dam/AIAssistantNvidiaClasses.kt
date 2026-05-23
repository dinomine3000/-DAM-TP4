package dam

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * GeminiAIAssistant class provides an interface to communicate with Google's Gemini AI models.
 * This class handles API authentication, request formatting, response parsing, and error handling.
 * It implements retry logic for rate-limited requests and validates JSON responses.
 *
 * @param properties Properties containing the API key for authentication with Gemini services
 */
class AIAssistantNvidiaClasses(override val properties: Properties) : AIAssistant {

    override fun getSystem() = "NVIDIA"
    override val apiKeyName = "NVIDIA_API_KEY"
    var newTemperature: Double? = null


    // Model selection - different models have different capabilities and costs
    // Data classes for Gemini API request structure

//    override var model = "meta/llama-4-maverick-17b-128e-instruct"
//    override var model = "nvidia/nemotron-3-nano-omni-30b-a3b-reasoning"
    override var model = "meta/llama-3.3-80b-instruct"
//    override var model = "qwen/qwen2.5-coder-32b-instruct"

    data class NvidiaRequest(
        val model: String = "meta/llama-4-maverick-17b-128e-instruct",
        val messages: List<Message>,
        val maxTokens: Int? = 512,
        val temperature: Double? = 1.0,
        val top_p: Double? = 1.0,
        val frequency_penalty: Double? = 0.0,
        val presence_penalty: Double? = 0.0,
        val stream: Boolean = false,
        val extraBody: Map<String, Any>? = null
    )

    data class Message(
        val role: String = "user",
        val content: String
    )
    // Gson instance for JSON serialization
    private val gson = Gson()

    /**
     * Constructs and formats a structured request from the given input prompt.
     * This method is intended to prepare the necessary request structure for
     * sending to an AI-powered model or API.
     *
     * @param prompt The user's input query or prompt that needs to be formatted into a request
     */
    override fun buildRequest(prompt: String): Request {
        // Create request structure using data classes
        val temp = newTemperature?:temperature
        val nvidiaRequest = NvidiaRequest(
            messages = listOf(Message(
                content = prompt
            )),
            top_p = 1.0,
            maxTokens = maxTokens,
            temperature = temp,
            frequency_penalty = 0.0,
            presence_penalty = 0.0,
            stream = false
        )

        // Convert to JSON string using Gson
        val requestBody = gson.toJson(nvidiaRequest)

        println("DEBUG temperature: $temp")

        // Configure the HTTP request with proper headers and authentication
        val request = Request.Builder()
            .url("https://integrate.api.nvidia.com/v1/chat/completions") // NVIDIA endpoint
            .addHeader("Authorization", "Bearer $apiKey") // Standard OAuth Bearer token
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()
        return request
    }
}