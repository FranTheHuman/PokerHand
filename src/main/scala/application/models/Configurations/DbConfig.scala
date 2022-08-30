package application.models.Configurations

case class DbConfig(
    driver: String = "",
    url: String = "",
    user: String = "",
    pass: String = ""
)
