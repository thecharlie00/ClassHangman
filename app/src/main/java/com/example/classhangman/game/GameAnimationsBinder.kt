package com.example.classhangman.game

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import com.example.classhangman.databinding.ActivityGameBinding


class GameAnimationsBinder(binding: ActivityGameBinding) {

    val jarss = binding.jarrones.children.toList() as ArrayList
    val allJars = binding.jarrones.children.toList()

    fun startAnimations(): GameAnimationsBinder {
        return this
    }

    fun failAnimation() {

        val jarrr = jarss.randomOrNull() ?: return
        jarss.remove(jarrr)
        ObjectAnimator.ofFloat(
            jarrr, "scaleX",
            0.5f, 2f, 0.5f, 4f,
        ).apply {
            duration = 1500
            repeatCount = ValueAnimator.RESTART
            repeatMode = ValueAnimator.REVERSE
            start()
        }.doOnEnd {
            ObjectAnimator.ofFloat(
                jarrr, "alpha",
                0f
            ).apply {
                duration = 2000
                start()
            }
        }
    }

    fun winGame() {
        allJars.forEach{
            if(it !in jarss){
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


}