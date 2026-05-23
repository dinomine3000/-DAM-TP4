package dam

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
class AIAssistantNvidia(override val properties: Properties) : AIAssistant {

    override fun getSystem() = "NVIDIA"
    override val apiKeyName = "NVIDIA_API_KEY"

    // Model selection - different models have different capabilities and costs
    //  override var model = "gemini-1.0-pro" // NOK - Primary model for most tasks
    // override var model = "gemini-1.0-ultra" // NOK - Most capable model (if available)
    // override var model = "gemini-1.5-flash" // OK - Faster, less expensive
    // override var model = "gemini-1.5-pro" // OK - Primary model for most tasks
    // override var model = "gemini-2.0-flash" // OK - Most capable model (if available)
    // override var model = "gemini-2.0-pro" // NOK - Most capable model (if available)
    // override var model = "gemini-2.5-flash" // NOK - Most capable model (if available)
    // override var model = "gemini-2.5-flash-preview" // NOK - Most capable model (if available) //override var model = "gemini-2.5-flash-preview-04-17" // NOK - Most capable model (if available)
    override var model = "meta/llama-4-maverick-17b-128e-instruct"
    /**
     * Constructs and formats a structured request from the given input prompt.
     * This method is intended to prepare the necessary request structure for
     * sending to an AI-powered model or API.
     *
     * @param prompt The user's input query or prompt that needs to be formatted into a request
     */
    override fun buildRequest(prompt: String): Request {
        // Create the content array with the parts of the prompt
        val messagesArray = JSONArray()
            .put(
                JSONObject()
                    .put("role", "user")
                    .put("content", prompt) // NVIDIA/OpenAI uses 'content', Gemini used 'parts/text'
            )

        // Build the complete JSON payload with all parameters from your Python script
        val requestBody = JSONObject()
            .put("model", model)
            .put("messages", messagesArray)
            .put("max_tokens", 512)
            .put("temperature", temperature)
            .put("top_p", 1.00)
            .put("frequency_penalty", 0.00)
            .put("presence_penalty", 0.00)
            .put("stream", false)
            .toString() // Convert to JSON string

        // Configure the HTTP request targeting NVIDIA's endpoint with Bearer Authentication
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