package eu.wanderclan.capitalquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val Ink = Color(0xFF172E39)
private val Teal = Color(0xFF006D68)
private val Mint = Color(0xFFE2F4ED)
private val Cream = Color(0xFFF8F5EC)
private val Coral = Color(0xFFB75040)
private const val ROUND_SIZE = 10

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { CapitalQuest() } }
    }
}

@Composable
private fun CapitalQuest() {
    val deck = remember { QuizDeck() }
    var mode by remember { mutableStateOf(Direction.MIXED) }
    var german by remember { mutableStateOf(Locale.getDefault().language == "de") }
    var question by remember { mutableStateOf(deck.next(mode, german)) }
    var selected by remember { mutableStateOf<String?>(null) }
    var round by remember { mutableIntStateOf(1) }
    var score by remember { mutableIntStateOf(0) }

    fun restart(newMode: Direction = mode) {
        mode = newMode
        round = 1
        score = 0
        selected = null
        question = deck.next(newMode, german)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Cream).verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 26.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(42.dp).clip(CircleShape).background(Teal), contentAlignment = Alignment.Center) {
                Text("✦", color = Color.White, fontSize = 24.sp)
            }
            Column {
                Text("CAPITAL QUEST", color = Ink, fontWeight = FontWeight.Black, fontSize = 20.sp, letterSpacing = 1.sp)
                Text(if (german) "Eine kleine Reise durch Europa" else "A little journey across Europe", color = Teal, fontSize = 13.sp)
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(if (german) "Wähle deinen Weg" else "Choose your route", color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            Text(if (german) "DE · EN" else "EN · DE", color = Teal, fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { german = !german; restart() }.padding(8.dp))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            listOf(Direction.MIXED to if (german) "Mix" else "Mix", Direction.COUNTRY_TO_CAPITAL to if (german) "Hauptstädte" else "Capitals", Direction.CAPITAL_TO_COUNTRY to if (german) "Länder" else "Countries")
                .forEach { (choice, label) ->
                    val active = mode == choice
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                            .background(if (active) Teal else Color.White)
                            .clickable { if (mode != choice) restart(choice) }
                            .padding(vertical = 11.dp), contentAlignment = Alignment.Center
                    ) {
                        Text(label, color = if (active) Color.White else Ink, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${if (german) "FRAGE" else "QUESTION"} $round / $ROUND_SIZE", color = Teal, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("$score ${if (german) "richtig" else "correct"}", color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(ROUND_SIZE) { index ->
                Box(Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(4.dp))
                    .background(if (index < round) Teal else Color(0xFFDDE5DF)))
            }
        }
        Surface(shape = RoundedCornerShape(26.dp), color = Ink, shadowElevation = 10.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.background(Brush.linearGradient(listOf(Ink, Color(0xFF245F60)))).padding(26.dp)) {
                Text(if (question.direction == Direction.COUNTRY_TO_CAPITAL) if (german) "FINDE DIE HAUPTSTADT" else "FIND THE CAPITAL" else if (german) "FINDE DAS LAND" else "FIND THE COUNTRY",
                    color = Color(0xFFABD9C9), fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                Spacer(Modifier.height(18.dp))
                Text(question.displayPrompt,
                    color = Color.White, fontSize = 34.sp, lineHeight = 40.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                Text(if (question.direction == Direction.COUNTRY_TO_CAPITAL) if (german) "Welche Stadt ist die Hauptstadt?" else "Which city is its capital?" else if (german) "Zu welchem Land gehört diese Hauptstadt?" else "Which country has this capital?",
                    color = Color(0xFFE2F4ED), fontSize = 15.sp)
            }
        }
        Text(if (german) "Wähle eine Antwort" else "Pick one answer", color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        question.options.forEachIndexed { index, option ->
            val revealed = selected != null
            val right = revealed && option == question.answer
            val wrong = revealed && option == selected && !right
            val highlight = when { right -> Mint; wrong -> Color(0xFFFFEAE5); else -> Color.White }
            val outline = when { right -> Teal; wrong -> Coral; else -> Color(0xFFE3E8E3) }
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(highlight)
                    .border(1.dp, outline, RoundedCornerShape(18.dp))
                    .clickable(enabled = !revealed) {
                        selected = option
                        if (option == question.answer) score++
                    }.padding(horizontal = 17.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("${'A' + index}", color = outline, fontWeight = FontWeight.Bold)
                Text(question.displayOption(option),
                    color = Ink, fontWeight = FontWeight.Medium, fontSize = 17.sp, modifier = Modifier.weight(1f))
                if (right || wrong) Text(if (right) "✓" else "×", color = outline, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
        if (selected != null) {
            Text(if (selected == question.answer) if (german) "Sehr gut! ✦" else "Lovely work! ✦" else if (german) "Richtig ist ${question.answer}." else "The answer is ${question.answer}.",
                color = if (selected == question.answer) Teal else Coral, fontWeight = FontWeight.Bold)
            Button(onClick = {
                if (round == ROUND_SIZE) restart()
                else { round++; selected = null; question = deck.next(mode, german) }
            }, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Teal)) {
                Text(if (round == ROUND_SIZE) if (german) "Noch einmal  ·  $score / $ROUND_SIZE" else "Play again  ·  $score / $ROUND_SIZE" else if (german) "Weiter →" else "Next stop →", fontSize = 16.sp)
            }
        }
        Text("${Europe.places.size} ${if (german) "Orte entdecken · Keine Werbung · Offline" else "places to explore · No ads · Works offline"}", color = Color(0xFF667C7C),
            fontSize = 12.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}
