import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.result.Result

class ViaCepUtils {
    fun obterUFeEstadoPorCEP(cep: String?): Map<String, String?>? {
        val url = "https://viacep.com.br/ws/$cep/json/"

        val (_, _, result) = Fuel.get(url).responseObject(MapDeserializer())

        return when (result) {
            is Result.Success -> {
                val uf = result.value["UF"]
                val estado = result.value["Estado"]
                val regiao = obterRegiaoPorUF(uf)
                mapOf("UF" to uf, "Estado" to estado, "Regiao" to regiao)
            }
            is Result.Failure -> {
                println("Erro ao obter dados do CEP: ${result.error}")
                null
            }
        }
    }

    private fun obterRegiaoPorUF(uf: String?): String {
        return when (uf) {
            "AC", "AM", "AP", "PA", "RO", "RR", "TO" -> "Norte"
            "AL", "BA", "CE", "MA", "PB", "PE", "PI", "RN", "SE" -> "Nordeste"
            "DF", "GO", "MS", "MT" -> "Centro-Oeste"
            "ES", "MG", "RJ", "SP" -> "Sudeste"
            "PR", "RS", "SC" -> "Sul"
            else -> "Região não encontrada"
        }
    }
}

class MapDeserializer : ResponseDeserializable<Map<String, String>> {
    override fun deserialize(content: String): Map<String, String>? {
        val jsonResponse = ObjectMapper().readTree(content)
        val uf = jsonResponse?.get("uf")?.asText()
        val estado = jsonResponse?.get("localidade")?.asText()

        return if (uf != null && estado != null) {
            mapOf("UF" to uf, "Estado" to estado)
        } else {
            null
        }
    }
}
//
//fun main() {
//    val cep = "80010000" // Exemplo de CEP
//    val viaCep = ViaCepUtils()
//
//    val resultMap = viaCep.obterUFeEstadoPorCEP(cep)
//
//    if (resultMap != null) {
//        println("Resultado: $resultMap")
//    } else {
//        println("UF e Estado não disponíveis.")
//    }
//}
