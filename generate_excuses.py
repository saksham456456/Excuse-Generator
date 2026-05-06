import json
import random

# Base components for excuses
subjects = ["My dog", "My cat", "My goldfish", "The neighbor's kid", "A wild pigeon", "My internet router", "My laptop", "My phone", "A mysterious hacker", "The weather", "My alarm clock", "My inner demon", "My brain", "A time traveler", "An alien"]
actions = ["ate", "destroyed", "deleted", "hijacked", "froze", "melted", "ran away with", "set fire to", "refused to process", "forgot about", "accidentally sent to space"]
objects = ["my homework", "the project files", "my motivation", "my alarm", "my meeting link", "my presentation", "my memory", "my schedule", "my keys", "the WiFi password"]
excuses_tail = [
    "... so yeah, I can't make it.",
    "... it's a tragedy, really.",
    "... please respect my privacy during this difficult time.",
    "... honestly, I'm just as surprised as you.",
    "... what can you do, right?",
    "... let's just pretend this never happened.",
    "... I'll try again tomorrow.",
    "... send help."
]

categories = ["School", "Work", "Relationships", "Savage", "Tech Issues", "Introvert Problems", "Strict Boss", "Clingy Ex", "Toxic Savage"]

excuses = []

# Generate 1000 unique excuses
generated_set = set()

def generate_excuse(category):
    if category == "Savage" or category == "Toxic Savage":
        savage_starts = ["Honestly,", "Look,", "Listen,", "To be brutally honest,", "Not gonna lie,"]
        savage_mids = ["I just didn't want to.", "I value my own time more than yours.", "I'd rather stare at a wall.", "this sounds like a 'you' problem.", "I forgot you existed for a moment."]
        text = f"{random.choice(savage_starts)} {random.choice(savage_mids)}"
    elif category == "Introvert Problems":
        intro_starts = ["My social battery is at 1%.", "I tried to leave the house but my bed held me hostage.", "The thought of interacting with humans right now is terrifying.", "I need a 3-business-day recovery period from my last social interaction."]
        text = f"{random.choice(intro_starts)} {random.choice(excuses_tail)}"
    elif category == "Tech Issues":
        tech_starts = ["My WiFi has decided to strike.", "My laptop is currently updating and is stuck at 99%.", "Zoom crashed my entire operating system.", "A ghost in the machine is preventing me from logging in."]
        text = f"{random.choice(tech_starts)} {random.choice(excuses_tail)}"
    else:
        text = f"{random.choice(subjects)} {random.choice(actions)} {random.choice(objects)} {random.choice(excuses_tail)}"

    # Capitalize first letter
    text = text[0].upper() + text[1:]
    return text

while len(excuses) < 1050:
    cat = random.choice(categories)
    text = generate_excuse(cat)
    if text not in generated_set:
        generated_set.add(text)
        excuses.append({
            "id": len(excuses) + 1,
            "category": cat,
            "text": text
        })

with open('app/src/main/assets/excuses.json', 'w') as f:
    json.dump(excuses, f, indent=4)

print(f"Generated {len(excuses)} excuses.")
