package gcubeit.com.enerwireproduccionv13.util

import java.io.IOException

class NoConnectivityException(
    message: String = "Red no disponible, favor verificar WiFi o Conexion de Datos"
) : IOException(message)

class SlowConnectionException (
    message: String = "La conexion es demasiado lenta. Por favor, intentalo de nuevo mas tarde."
) : Exception(message)

class NoConnectionException (
    message: String = "No hay conexion a internet. Por favor, conecta a una red y vuelve a intentarlo"
) : Exception(message)