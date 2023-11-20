package com.example.calendar

sealed class NavRoutes (val route: String) {
    object CalendarView : NavRoutes("calendar-view")
    object DayView : NavRoutes("day-view")
    object EventView : NavRoutes("event-view")
    object CreateEvent : NavRoutes("create-event")
    object EditEvent : NavRoutes("edit-event")
    object MonthView : NavRoutes("monthView")
}
