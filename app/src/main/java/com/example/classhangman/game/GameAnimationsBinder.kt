package com.example.classhangman.game

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import com.example.classhangman.databinding.ActivityGameBinding


class GameAnimationsBinder(binding: ActivityGameBinding) {

    val jailbars = binding.jailbars.children.toList() as ArrayList
    val allJailbars = binding.jailbars.children.toList()
    val ghosts = listOf(binding.fantasma1, binding.fantasma2, binding.fantasma3)

    fun startAnimations(): GameAnimationsBinder {
        ghosts.forEach {
            ghostAnimation(it)
        }
        return this
    }

    private fun ghostAnimation(ghost: ImageView) {
        ObjectAnimator.ofFloat(
            ghost, "translationY",
            ghost.y + 30f,
        ).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }
    }

    var lockAnimation = false // avoid two ghosts attacking at the same time
    fun failAnimation() {

        // Ghost attack
        if (!lockAnimation) {
            lockAnimation = true

            val ghost = ghosts.random()
            ObjectAnimator.ofFloat(
                ghost, "scaleX",
                3f,
            ).apply {
                repeatCount = ValueAnimator.RESTART
                repeatMode = ValueAnimator.REVERSE
                start()
            }.doOnEnd {
                lockAnimation = false
            }
            ObjectAnimator.ofFloat(
                ghost, "scaleY",
                3f,
            ).apply {
                repeatCount = ValueAnimator.RESTART
                repeatMode = ValueAnimator.REVERSE
                start()
            }
        }

        // jailbars animations
        val jailbar = jailbars.randomOrNull() ?: return
        jailbars.remove(jailbar)
        ObjectAnimator.ofFloat(
            jailbar, "scaleX",
            0.5f, 2f, 0.5f, 2f, 0.5f, 2f, 0.5f, 2f, 0.5f, 2f,
        ).apply {
            duration = 1500
            repeatCount = ValueAnimator.RESTART
            repeatMode = ValueAnimator.REVERSE
            start()
        }.doOnEnd {
            ObjectAnimator.ofFloat(
                jailbar, "alpha",
                0f
            ).apply {
                duration = 2000
                start()
            }
        }
    }

    fun winGame() {
        allJailbars.forEach {
            if (it !in jailbars) {
                it.alpha = 1f
                ObjectAnimator.ofFloat(
                    it, "scaleX",
                    0f, 1f
                ).apply {
                    duration = 1000
                    start()
                }
            }
        }
    }

    fun loseGame() {
        val ghost = ghosts.random()
        ghost.clearAnimation()
        ghost.updateLayoutParams<ConstraintLayout.LayoutParams> {
            horizontalBias = 0.5f
            verticalBias = 1f
        }
        ObjectAnimator.ofFloat(
            ghost, "scaleX",
            3f,
        ).start()
        ObjectAnimator.ofFloat(
            ghost, "scaleY",
            3f,
        ).apply {
            start()
        }
    }
}