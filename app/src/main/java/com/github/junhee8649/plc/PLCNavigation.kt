import androidx.navigation.NavHostController

/**
 * Navigation 관련 화면들의 이름
 */
object PLCScreens {
    const val MAIN_SCREEN = "main"
}

/**
 * Destinations (라우트 정의)
 */
object PLCDestinations {
    const val MAIN_ROUTE = PLCScreens.MAIN_SCREEN
}


/**
 * 모든 네비게이션 동작 관리
 */
class PLCNavigationActions(private val navController: NavHostController) {

    // 인증 플로우 - 이전 화면 제거하며 진행
//    fun navigateToSignIn() {
//        navController.navigate(PLCDestinations.MAIN_ROUTE) {
//            popUpTo(navController.graph.startDestinationId) { inclusive = true }
//        }
//    }


}