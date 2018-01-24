# Giphy Trend

Sample Android app showing list of trending gifs from giphy.com written entirely on Kotlin.

In order to function properly this app requires valid Giphy API key. You can obtain one [here](https://developers.giphy.com/).
Replace `API_KEY` in `gradle.properties` with your key.

# Architecture

This app uses adapted [Redux](https://redux.js.org) architecture combined with Reactive Extensions framework [RxJava](https://github.com/ReactiveX/RxJava).

### Core principles:

Every `Activity` has it's own `State` and `Store`. `Activity` uses `ViewBinder` to bind `View` with the `Store` by dispatching actions from
`View` to `Store` and render `State` on `View` when it changes.

`Reducers` are not allowed to perform side effects, it is an interface with a single function:

`fun reduce(state: State, action: Action): State`

Since there are no side effects in `Reducers` there is a special component designed exactly for this - `Middleware`. In this project `Middleware` API differs a bit from
[tradional Redux Middleware API](https://redux.js.org/docs/advanced/Middleware.html). `Middleware` interface has single `create` function:

`fun create(actions: Observable<Action>, state: Observable<State>): Observable<Action>`

It accepts 2 "streams" - `Action` and `State` and returns "stream" of `Action`. This way it has an ability to react to incoming `Action`s and `State` changes and emit new `Action`s.
`Middleware` is designed with asynchronous operations in mind, but you can perform synchronous operations as well.

# License

MIT