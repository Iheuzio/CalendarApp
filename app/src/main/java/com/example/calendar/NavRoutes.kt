package com.example.calendar

sealed class NavRoutes (val route: String) {
    object CalendarView : NavRoutes("calendar-view")
    object DayView : NavRoutes("day-view")
    object EventView : NavRoutes("event-view")
    object CreateEditEvent : NavRoutes("create-edit-event")
    object MonthView : NavRoutes("monthView")

}
