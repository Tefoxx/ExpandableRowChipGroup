<h1 align="center">ExpandableRowChipGroup</h1>

<p align="center">
    <b>A customizable Android view based on <code>ChipGroup</code>, designed to limit rows of chips and add a toggle chip for showing or hiding additional content.</b>
</p>

---

<h2>Features</h2>

<ul>
    <li><b>Row limitation:</b> Restrict the number of visible chip rows to improve layout management.</li>
    <li><b>Expandable toggle chip:</b> Optionally adds a chip for expanding or collapsing, depending on the current state. The toggle chip dynamically adjusts to reflect the number of hidden chips or can be omitted entirely if not needed.</li>
    <li><b>Responsiveness:</b> Automatically calculates the size of the ChipGroup to properly arrange the chips based on the available space.</li>
    <li><b>Easy integration:</b> Built on <code>ChipGroup</code>, leveraging its familiar API.</li>
</ul>

---

## Requirements

- **Minimum SDK version**: Android 8.0 (API level 26).

<h2>Installation</h2>

<p>To integrate <b>ExpandableRowChipGroup</b> into your project, add the following to your <code>build.gradle</code> files:</p>

<p>1. Add JitPack repository</p>

    repositories {
        maven("https://jitpack.io")
    }


<p>2. Add the dependency</p>

    dependencies {
        implementation("com.github.Tefoxx:ExpandableRowChipGroup:1.0.8")
    }
[![](https://jitpack.io/v/Tefoxx/ExpandableRowChipGroup.svg)](https://jitpack.io/#Tefoxx/ExpandableRowChipGroup)
---

<h2>Usage</h2>

### XML Attributes

`ExpandableRowChipGroup` provides the following custom attributes:

| Attribute              | Description                                                                                    | Default Value |
|------------------------|------------------------------------------------------------------------------------------------|---------------|
| `app:expandChipLayout` | Layout resource for the "expand" chip (e.g., "More").                                          | nothing       |
| `app:hideChipLayout`   | Layout resource for the "hide" chip (e.g., "Hide").                                            | nothing       |
| `app:maxRows`          | Maximum number of visible rows                                                                 | Int.MAX_VALUE |

### Public Methods

`ExpandableRowChipGroup` provides the following methods for managing chips and their behavior:

| Method                                                                                         | Description                                                                                                     |
|------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------|
| `setChips(chips: List<Chip>)`                                                                  | Dynamically sets the list of chips to display. Clears any previously added chips.                               |
| `setControlButtonText(onControlButtonAppear: ((state: State, hiddenChips: Int) -> String)?)`   | Customizes the text of the control button ("More N+" / "Hide") dynamically based on the state and hidden chips. |
| `getCheckedChip(): Chip?`                                                                      | Returns the currently checked chip, if any, or `null` if none are selected.                                     |

<h3>XML Example</h3>

```
<com.ent21.expandablerowchipgroup.ExpandableRowChipGroup
        android:id="@+id/expandableRowChipGroup"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:chipSpacingHorizontal="8dp"
        app:chipSpacingVertical="8dp"
        app:expandChipLayout="@layout/expand_chip"
        app:hideChipLayout="@layout/hide_chip"
        app:maxRows="2" />
```
expand_chip.xml
```
<com.google.android.material.chip.Chip xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Show"
    android:textColor="#FFFFFFe"
    app:chipBackgroundColor="#000000"
    app:chipCornerRadius="16dp"
    app:chipStrokeWidth="0dp"
    app:chipSurfaceColor="@android:color/transparent" />
```
hide_chip.xml
```
<com.google.android.material.chip.Chip xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Hide"
    android:textColor="#FFFFFFe"
    app:chipBackgroundColor="#000000"
    app:chipCornerRadius="16dp"
    app:chipStrokeWidth="0dp"
    app:chipSurfaceColor="@android:color/transparent" />
```

<h3>Kotlin Example</h3>
<p>If you need to create dynamic chips:</p>

    val chipGroup = findViewById<ExpandableRowChipGroup>(R.id.expandableRowChipGroup)
    val chips = List(16) {
        val chip = inflater.inflate(
            R.layout.your_custom_chip,
            expandableChipGroup,
            false
        ) as Chip
        chip.text = "Chip #$it"
        chip
    }
    expandableChipGroup.setChips(chips)

<p>If you need to set the text for control chips:</p>

    val chipGroup = findViewById<ExpandableRowChipGroup>(R.id.expandableRowChipGroup)
    chipGroup.setControlButtonText { state, hiddenChips ->
        when (state) {
            ExpandableRowChipGroup.State.EXPANDED -> "Hide"
            ExpandableRowChipGroup.State.COLLAPSED -> "Show ($hiddenChips)"
        }
    }

---

<h2>Demo</h2>

<p>Check out how <b>ExpandableRowChipGroup</b> works:</p>


![Demo](https://i.giphy.com/media/v1.Y2lkPTc5MGI3NjExbnE3MzY5MTZlMmU2MzA0cW9nNXQ1Z2ZocmQ3M21lcnZmamZtb2dxNiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/RTR0hXr5mGZQat6WdP/giphy.gif)

---

<h2>License</h2>

<p>This library is distributed under the <a href="LICENSE">MIT License</a>. Feel free to use it in your projects.</p>
