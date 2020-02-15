package com.example.movieticket.data.vos

class UserVO(
    var userId: String? = null,
    var userName: String? = null,
    var email: String? = null,
    var profileImageurl: String? = null,
    var bookings: HashMap<String,BookingsVO>? = null
) {

    companion object {
        fun initUser(
            uid: String,
            userName: String,
            email: String,
            photoUrl: String
        ): UserVO {
            val userVO = UserVO()
            userVO.userId = uid
            userVO.userName = userName
            userVO.email = email
            userVO.profileImageurl = photoUrl
            return userVO
        }
    }
}