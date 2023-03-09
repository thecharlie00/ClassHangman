package com.example.classhangman.game

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import com.example.classhangman.databinding.ActivityGameBinding

class GameAnimationsBinder(binding: ActivityGameBinding) {

    var jailbars = binding.jailbars.children.toList() as ArrayList
    val ghosts = listOf(binding.fantasma1, binding.fantasma2, binding.fantasma3)

    fun startAnimations(): GameAnimationsBinder {
        ghosts.forEach {
            ghostAnimation(it)
        }
        return this
    }

    private fun ghostAnimation(ghost: ImageView) {
        ObjectAnimator.ofFloat(
            ghost, "y",
            ghost.y + 30f,
        ).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }
    }

    fun failAnimation() {
        // Ghost attack
        val ghost = ghosts.random()
        ObjectAnimator.ofFloat(
            ghost, "scaleX",
            3f,
        ).apply {
            repeatCount = ValueAnimator.RESTART
            repeatMode = ValueAnimator.REVERSE
            start()
        }
        ObjectAnimator.ofFloat(
            ghost, "scaleY",
            3f,
        ).apply {
            repeatCount = ValueAnimator.RESTART
            repeatMode = ValueAnimator.REVERSE
            start()
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

            if (jailbars.isEmpty())
                loseGame(ghost)
        }
    }

    private fun loseGame(ghost: ImageView) {
        ghost.clearAnimation()
        ObjectAnimator.ofFloat(
            ghost, "scaleX",
            3f,
        ).start()
        ObjectAnimator.ofFloat(
            ghost, "scaleY",
            3f,
        ).apply {
//            doOnEnd {
//                ObjectAnimator.ofFloat(
//                    ghost, "y",
//                    ghost.y + 300f,
//                ).start()
//            }
            start()
        }
    }
}