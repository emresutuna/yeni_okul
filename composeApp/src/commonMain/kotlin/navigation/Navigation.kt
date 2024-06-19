package navigation

import BACK_CLICK_ROUTE
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ui.HomePage
import ui.ProfilePage
import yeniokul.composeapp.generated.resources.Res
import yeniokul.composeapp.generated.resources.ic_home
import yeniokul.composeapp.generated.resources.ic_profile


@Composable
fun HomeNav() {
    val navController = rememberNavController()

    NavHostMain(
        navController = navController,
        onNavigate = { routeName ->
            navigateTo(routeName, navController)
        }
    )
}

@Composable
fun NavHostMain(
    navController: NavHostController = rememberNavController(),
    onNavigate: (rootName: String) -> Unit,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination

    Scaffold(
        topBar = {
            val title = getTitle(currentScreen)
            TopBar(
                title = title,
                canNavigateBack = currentScreen?.route == AppScreen.Detail.route,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            composable(route = BottomBarScreen.Home.route) {
                HomePage(onNavigate = onNavigate)
            }
            composable(route = BottomBarScreen.Reels.route) {
                ProfilePage(onNavigate = onNavigate)
            }
        }
    }
}

fun getTitle(currentScreen: NavDestination?): String {
    return when (currentScreen?.route) {
        BottomBarScreen.Home.route -> {
            "Home"
        }

        BottomBarScreen.Reels.route -> {
            "Reels"
        }

        BottomBarScreen.Profile.route -> {
            "Profile"
        }

        AppScreen.Detail.route -> {
            "Detail"
        }

        else -> {
            ""
        }
    }
}

fun navigateTo(
    routeName: String,
    navController: NavController
) {
    when (routeName) {
        BACK_CLICK_ROUTE -> {
            navController.popBackStack()
        }

        else -> {
            navController.navigate(routeName)
        }
    }
}

sealed class AppScreen(val route: String) {
    data object Detail : AppScreen("nav_detail")
}

sealed class BottomBarScreen(
    val route: String,
    var title: String,
    val defaultIcon: DrawableResource
) {
    data object Home : BottomBarScreen(
        route = "HOME",
        title = "Home",
        defaultIcon = Res.drawable.ic_home,
    )

    data object Reels : BottomBarScreen(
        route = "REELS",
        title = "Reels",
        defaultIcon = Res.drawable.ic_profile,
    )

    data object Profile : BottomBarScreen(
        route = "PROFILE",
        title = "Profile",
        defaultIcon = Res.drawable.ic_profile,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back_button"
                    )
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
) {
    val homeItem = BottomBarScreen.Home
    val reelsItem = BottomBarScreen.Reels
    val profileItem = BottomBarScreen.Profile

    val screens = listOf(
        homeItem,
        reelsItem,
        profileItem
    )

    AppBottomNavigationBar(show = navController.shouldShowBottomBar) {
        screens.forEach { item ->
            AppBottomNavigationBarItem(
                icon = item.defaultIcon,
                label = item.title,
                onClick = {
                    navigateBottomBar(navController, item.route)
                },
                selected = navController.currentBackStackEntry?.destination?.route == item.route
            )
        }
    }
}

@Composable
fun AppBottomNavigationBar(
    modifier: Modifier = Modifier,
    show: Boolean,
    content: @Composable (RowScope.() -> Unit),
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.windowInsetsPadding(BottomAppBarDefaults.windowInsets)
    ) {
        if (show) {
            Column {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .selectableGroup(),
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@Composable
fun RowScope.AppBottomNavigationBarItem(
    modifier: Modifier = Modifier,
    icon: DrawableResource,
    label: String,
    onClick: () -> Unit,
    selected: Boolean,
) {
    Column(
        modifier = modifier
            .weight(1f)
            .clickable(
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = icon.toString(),
            contentScale = ContentScale.Crop,
            colorFilter = if (selected) {
                ColorFilter.tint(MaterialTheme.colorScheme.primary)
            } else {
                ColorFilter.tint(MaterialTheme.colorScheme.outline)
            },
            modifier = modifier.then(
                Modifier.clickable {
                    onClick()
                }
                    .size(24.dp)
            )
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
    }
}

private fun navigateBottomBar(navController: NavController, destination: String) {
    navController.navigate(destination) {
        navController.graph.startDestinationRoute?.let { route ->
            popUpTo(BottomBarScreen.Home.route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}

private val NavController.shouldShowBottomBar
    get() = when (this.currentBackStackEntry?.destination?.route) {
        BottomBarScreen.Home.route,
        BottomBarScreen.Reels.route,
        BottomBarScreen.Profile.route,
        -> true

        else -> false
    }