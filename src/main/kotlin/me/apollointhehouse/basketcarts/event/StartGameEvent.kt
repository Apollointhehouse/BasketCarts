package me.apollointhehouse.basketcarts.event

import me.apollointhehouse.raywire.api.Event

sealed class StartGameEvent : Event {
	object Before : StartGameEvent()
	object After : StartGameEvent()
}
