package com.mark.shereheke.navigation

// ─── Route constants ─────────────────────────────────────────────────────────
const val ROUTE_SPLASH        = "splash"
const val ROUTE_LOGIN         = "login"
const val ROUTE_REGISTER      = "register"
const val ROUTE_DASHBOARD     = "dashboard"
const val ROUTE_LIST_EVENTS   = "list_events"
const val ROUTE_CREATE_EVENT  = "create_event"
const val ROUTE_UPDATE_EVENT  = "update_event/{eventId}"
const val ROUTE_EVENT_DETAIL  = "event_detail/{eventId}"

// Helper to build parameterised routes at call sites
fun updateEventRoute(eventId: String) = "update_event/$eventId"
fun eventDetailRoute(eventId: String) = "event_detail/$eventId"
