package com.startars.smart_healthy_wallet.domain.ai

object PromptTemplates {

    /**
     * Highly engineered system prompt template.
     * Enforces explicit, unescaped JSON output rules with structural target schemas.
     */
    fun buildMedicalReceiptPrompt(rawOcrText: String): String {
        return """
        <bos><start_of_turn>user
        You are a precise, localized health insurance auditor. Your sole task is to parse raw text extracted from a medical document into a clean data layout. Do not include markdown code block syntax. Output raw minified JSON only.

        Expected JSON structure format exactly matching these keys:
        {
          "provider": "Name of the clinic/hospital or pharmacy",
          "total_amount": 0.00,
          "has_billing_error": false,
          "items": [
            { "name": "Item description", "cost": 0.00, "category": "Pharmacy/Lab/Consultation" }
          ]
        }

        ###
        Example Input:
        ST. STAR MEDICAL CENTER
        DATE: 10/24/2026
        BLOOD PANEL TESTS: $140.00
        ADMIN FEE: $45.00
        TOTAL: $185.00
        
        Example Output:
        {"provider":"ST. STAR MEDICAL CENTER","total_amount":185.00,"has_billing_error":true,"items":[{"name":"BLOOD PANEL TESTS","cost":140.00,"category":"Lab"},{"name":"ADMIN FEE","cost":45.00,"category":"Administrative"}]}
        ###

        Parse this raw input text now:
        $rawOcrText
        <end_of_turn>
        <start_of_turn>model
        """.trimIndent()
    }
}
