package com.wk.unichat.WebRequests

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.wk.unichat.Utils.URL_LOGIN
import com.wk.unichat.Utils.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

// TWORZENIE POST REQUEST
object Requests {

    var isLogged = false
    var usrEmail = ""
    var logToken = ""

    // registerUser
    fun createUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        // JSON Body
        val jsonBody = JSONObject()
        // Tworzymy key-e
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        // Web Request
        val requestBody = jsonBody.toString() //zmieniamy na stringa

        // Method Type
        val requestCreation = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener { response -> // Response
            println(response)
            complete(true)
        }, Response.ErrorListener { error -> // Error Response
            Log.d("Error", "Creation fail $error" )
            complete(false)
        }){
            override fun getBodyContentType(): String { // Content Type
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(requestCreation) // Queque
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object: JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener {response->
            // Wyciąganie danych z naszego JSON-a
            Log.d("Success", "Creation completed $response" )

            // Tworzenie wyjątku, np. gdyby brakowało tokena user
            try {
                usrEmail = response.getString("user") // Wyciągamy stringa z el. o tagu "user"
                logToken = response.getString("token")
                isLogged = true
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "Exception: " + e.localizedMessage)
                complete(false)
            }

        },Response.ErrorListener {error->
            // Error
            Log.d("Error", "Creation fail $error" )
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(loginRequest)
    }
}