package ru.semper_viventem.chromeor.presentation.model

/**
 * @author Kulikov Konstantin
 * @since 24.01.2017.
 */
class LoginEntity {
    lateinit var actionUrl: String
    lateinit var originUrl: String
    lateinit var usernameValue: String
    lateinit var passwordValue: String

    override fun toString(): String {
        return "LoginEntity(actionUrl='$actionUrl', originUrl='$originUrl', usernameValue='$usernameValue', passwordValue='$passwordValue')"
    }


}