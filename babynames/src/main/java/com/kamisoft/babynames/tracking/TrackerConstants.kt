package com.kamisoft.babynames.tracking

/***
 * VERY IMPORTANT:

 * ********************* FIREBASE CONSIDERATIONS ********************

 * EVENTS:
 * Event names can be up to 32 characters long, may only contain alphanumeric characters and
 * underscores ("_"), and must start with an alphabetic character. The "firebase_" prefix is
 * reserved and should not be used.

 * The following event names are reserved and cannot be used:
 * - app_clear_data
 * - app_uninstall
 * - app_update
 * - error
 * - first_open
 * - in_app_purchase
 * - notification_dismiss
 * - notification_foreground
 * - notification_open
 * - notification_receive
 * - os_update
 * - session_start
 * - user_engagement

 * PARAMS:
 * Param names can be up to 24 characters long, may only contain alphanumeric characters and
 * underscores ("_"), and must start with an alphabetic character. Param values can be up to 36
 * characters long. The "firebase_" prefix is reserved and should not be used.
 */

class TrackerConstants {

    enum class Section(val value: String) {
        MAIN("main"),
        PARENTS_SETUP("parents_setup"),
        FIND_MATCHES("find_matches"),
        NAMES_LIST("name_list"),
        CONTACT("contact");

        enum class Main(val value: String) {
            MAIN("s_main")
        }

        enum class ParentsSetup(val value: String) {
            MAIN("s_parents_main")
        }

        enum class FindMatches(val value: String) {
            CHOOSE_GENDER("s_choose_gender"),
            WHO_CHOOSE("s_who_choose"),
            FIRST_PARENT_CHOOSE_NAME("s_first_parent_choose"),
            SECOND_PARENT_CHOOSE_NAME("s_second_parent_choose"),
            MATCHES("s_matches");
        }

        enum class NamesList(val value: String) {
            BOY_NAMES("s_boy_names_list"),
            GIRL_NAMES("s_girl_names_list"),
        }

        enum class Contact(val value: String) {
            MAIN("s_contact_main")
        }
    }

    enum class Action(val value: String) {
        CLICK("click"),
        ACCEPT("accept"),
        CANCEL("cancel"),
        FINISH("finish"),
        SHOW("show");

        companion object {
            fun get(value: String): Action {
                return Action.values().find { it.value == value } ?: CLICK
            }
        }
    }

    enum class Param(val value: String) {
        SCREEN("screen"),
        ACTION("action");
    }

    class Label(val value: String) {

        enum class MainScreen(val value: String) {
            GO("main_go"),
            DRAWER_PARENTS_SCREEN("menu_parents"),
            DRAWER_BOY_NAMES_SCREEN("menu_boy_names"),
            DRAWER_GIRL_NAMES_SCREEN("menu_girl_names"),
            DRAWER_CONTACT_SCREEN("menu_contact"),
            NEW_APP_VERSION("main_new_version_dialog"),
            REQUIRED_APP_VERSION("main_required_version_dialog"),
            NEW_APP_OK("main_new_version_ok"),
            NEW_APP_DISMISSED("main_new_version_dismissed"),
            REQUIRED_APP_OK("main_required_version_ok"),
            REQUIRED_APP_DISMISSED("main_required_version_dismissed")
        }

        enum class ParentsScreen(val value: String) {
            PARENT1_DAD("parent1_dad"),
            PARENT1_MOM("parent1_mom"),
            PARENT2_DAD("parent2_dad"),
            PARENT2_MOM("parent2_mom"),
            GO_OK("parents_go_ok"),
            GO_KO("parents_go_kk")
        }

        enum class ChooseGenderScreen(val value: String) {
            BOY("boy"),
            GIRL("girl")
        }

        enum class WhoChooseScreen(val value: String) {
            PARENT1_DAD("who_dad1"),
            PARENT1_MOM("who_mom1"),
            PARENT2_DAD("who_dad2"),
            PARENT2_MOM("who_mom2"),
        }

        enum class FirstParentChooseScreen(val value: String) {
            FAVORITE_NAME("first_parent_favorite_name"),
            UNFAVORITE_NAME("first_parent_unfavorite_name"),
            SEARCH("first_parent_search")
        }

        enum class SecondParentChooseScreen(val value: String) {
            FAVORITE_NAME("second_parent_favorite_name"),
            UNFAVORITE_NAME("second_parent_unfavorite_name"),
            SEARCH("second_parent_search")
        }

        enum class ContactScreen(val value: String) {
            EMAIL_SUPPORT("contact_email_support")
        }
    }

}
