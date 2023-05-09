package com.example.classhangman.game

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import com.example.classhangman.databinding.ActivityGameBinding


class GameAnimationsBinder(binding: ActivityGameBinding) {

    val jarss = binding.jarrones.children.toList() as ArrayList
    val allJars = binding.jarrones.children.toList()

    fun startAnimations(): GameAnimationsBinder {
        return this
    }

    @SuppressLint("ObjectAnimatorBinding")
    fun failAnimation() {

        val jarrrr = jarss.randomOrNull() ?: return
        jarss.remove(jarrrr)
        ObjectAnimator.ofFloat(
            jarrrr, "scaleX",
            0.5f, 1f, 1.5f, 2f,
        ).apply {
            duration = 500
            start()
        }.doOnEnd {
            ObjectAnimator.ofInt(
                jarrrr, "Color",
                Color.WHITE
            ).apply {
                duration = 2000
                start()
            }
            ObjectAnimator.ofFloat(
                jarrrr, "alpha",
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