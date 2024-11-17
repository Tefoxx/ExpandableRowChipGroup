package com.ent21.expandablerowchipgroup

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.children
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

private const val MAX_ROWS_DEFAULT = Int.MAX_VALUE

class ExpandableRowChipGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ChipGroup(context, attrs, defStyleAttr) {

    private var maxRows = MAX_ROWS_DEFAULT
    private var state: State = State.COLLAPSED
        set(value) {
            field = value
            update()
        }
    private val hiddenChips get() = getAllChips().count { !it.isVisible }

    private val expandChip: Chip?
    private val hideChip: Chip?

    private var onControlButtonAppear: ((state: State, hiddenChips: Int) -> String)? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ExpandableRowChipGroup).apply {
            maxRows = getInt(R.styleable.ExpandableRowChipGroup_maxRows, MAX_ROWS_DEFAULT)
            expandChip = getLayoutChip(R.styleable.ExpandableRowChipGroup_expandChipLayout)
            hideChip = getLayoutChip(R.styleable.ExpandableRowChipGroup_hideChipLayout)
            state = if (getBoolean(R.styleable.ExpandableRowChipGroup_collapsed, true)) {
                State.COLLAPSED
            } else State.EXPANDED
            recycle()
        }
    }

    fun setChips(chips: List<Chip>) {
        removeAllViews()
        chips.forEach {
            addView(it)
        }
        update()
    }

    fun setControlButtonText(onControlButtonAppear: ((state: State, hiddenChips: Int) -> String)?) {
        this.onControlButtonAppear = onControlButtonAppear
    }

    fun getCheckedChip() = getAllChips().find { it.isChecked }

    private fun update() {
        rootView.post {
            when (state) {
                State.COLLAPSED -> collapse()
                State.EXPANDED -> expand()
            }
        }
    }

    private fun expand() {
        removeControlViews()
        val chips = getAllChips()
        chips.forEach {
            it.isVisible = true
        }
        if (willHiddenChipsWhenCollapsed() && hideChip != null) {
            hideChip.setControlText()
            hideChip.setOnClickListener {
                state = State.COLLAPSED
            }
            addView(hideChip)
        }
    }

    private fun willHiddenChipsWhenCollapsed(): Boolean {
        var currentRow = 1
        var currentWidth = 0
        val chips = getAllChips()
        val availableWidth = width - paddingLeft - paddingRight

        for (chip in chips) {
            val params = chip.layoutParams as LayoutParams
            val chipWidth = chip.width
            val chipWidthWithMargin = chipWidth + params.leftMargin + params.rightMargin

            if (currentWidth + chipWidthWithMargin > availableWidth) {
                if (++currentRow > maxRows) return true
                currentWidth = 0
            }
            currentWidth += (chipWidthWithMargin + chipSpacingHorizontal)
        }
        return false
    }

    private fun collapse() {
        removeControlViews()
        var currentRow = 1
        var currentWidth = 0
        val chips = getAllChips()
        val availableWidth = width - paddingLeft - paddingRight
        var lastVisibleRowChipOccupiedWidth = 0

        for (chip in chips) {
            val params = chip.layoutParams as LayoutParams
            val chipWidth = chip.width
            val chipWidthWithMargin = chipWidth + params.leftMargin + params.rightMargin

            if (currentWidth + chipWidthWithMargin > availableWidth) {
                if (currentRow == maxRows) {
                    lastVisibleRowChipOccupiedWidth = currentWidth
                }
                currentRow++
                currentWidth = 0
            }
            currentWidth += (chipWidthWithMargin + chipSpacingHorizontal)
            chip.isVisible = currentRow <= maxRows
        }

        if (hiddenChips > 0 && expandChip != null) {
            expandChip.setControlText()
            expandChip.setOnClickListener { state = State.EXPANDED }
            expandChip.visibility = INVISIBLE
            addView(expandChip)
            expandChip.doOnPreDraw {
                postCalculateExpandChipPosition(availableWidth, lastVisibleRowChipOccupiedWidth)
            }
            expandChip.post { expandChip.isVisible = true }
        }
    }

    private fun postCalculateExpandChipPosition(availableWidth: Int, rowChipOccupiedWidth: Int) {
        expandChip?.post {
            var lastVisibleRowChipOccupiedWidth = rowChipOccupiedWidth
            while (availableWidth - lastVisibleRowChipOccupiedWidth < expandChip.width) {
                val lastVisibleChip =
                    getAllChips().findLast { it.isVisible && it.id != expandChip.id } ?: throw Exception("No one chip is visible")
                val params = lastVisibleChip.layoutParams as LayoutParams

                val lastVisibleChipWidthWithMargin =
                    lastVisibleChip.width + params.leftMargin + params.rightMargin

                lastVisibleChip.isVisible = false
                lastVisibleRowChipOccupiedWidth -= (lastVisibleChipWidthWithMargin + chipSpacingHorizontal)
                if (availableWidth - lastVisibleRowChipOccupiedWidth >= expandChip.width) {
                    expandChip.setControlText()
                    postCalculateExpandChipPosition(availableWidth, lastVisibleRowChipOccupiedWidth)
                }
            }
        }
    }

    private fun removeControlViews() {
        removeView(hideChip)
        removeView(expandChip)
    }

    private fun Chip.setControlText() = setTextNotNull(onControlButtonAppear?.invoke(state, hiddenChips))

    private fun Chip.setTextNotNull(text: String?) = text?.let { this.text = it }

    private fun getAllChips(): List<Chip> = children.map { it as Chip }.toList()

    private fun TypedArray.getLayoutChip(attrId: Int) =
        getResourceId(attrId, -1).takeIf {
            it != -1
        }?.let {
            LayoutInflater.from(context).inflate(it, this@ExpandableRowChipGroup, false) as Chip
        }

    enum class State {
        EXPANDED, COLLAPSED
    }
}
