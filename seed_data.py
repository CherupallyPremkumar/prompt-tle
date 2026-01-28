import requests
import random
import json
import time

BASE_URL = "http://localhost:8080"
HEADERS = {"Content-Type": "application/json", "x-chenile-uid": "seeder"}

TASK_TYPES = [
    "code_generation", "debugging", "analysis", "refactoring",
    "data_transformation", "documentation", "testing", "other"
]

USERS = ["alice", "bob", "charlie", "david", "eve"]

TITLES = [
    "Optimize SQL Query for User Analytics", "Refactor Legacy Java Service", 
    "Debug React State Update Issue", "Generate Python Data Pipeline", 
    "Unit Tests for Payment Gateway", "Documentation for REST API",
    "Analyze Log Files for Errors", "Transform CSV to JSON",
    "Create Kubernetes Manifests", "Explain Complex Regex",
    "Secure Spring Boot Application", "Optimize React Performance",
    "Migrate Database Schema", "Implement Infinite Scroll",
    "Fix CSS Z-Index War", "Dockerize Node.js App",
    "Setup CI/CD Pipeline", "Write Integration Tests",
    "Explain async/await pattern", "Generate Mock Data Script"
]

DESCRIPTIONS = [
    "I need help optimizing a complex SQL query that joins 5 tables.",
    "The current implementation is messy and hard to maintain.",
    "The state is not updating correctly when I click the button.",
    "Need a script to ingest large CSV files and output newline delimited JSON.",
    "Write comprehensive tests for the payment processing module.",
    "Document the endpoints for the user management service.",
    "Find the root cause of the NPE in the attached log file.",
    "Convert this messy CSV into a clean JSON structure.",
    "Need deployment configs for a standard web app.",
    "What does this regex actually do? It matches emails I think.",
    "Ensure the application is protected against OWASP Top 10.",
    "The app is laggy on mobile devices, need profiling help.",
    "Help me move from MySQL to PostgreSQL.",
    "Implement loading more items as the user scrolls down.",
    "My modal is appearing behind the navbar.",
    "Create a Dockerfile for a standard Express app.",
    "Configure GitHub Actions to build and test on push.",
    "Verify that the user flow works end-to-end.",
    "Explain how promises work in JavaScript.",
    "Generate 50 users with random names and emails."
]

def create_prompt(index):
    task_type = random.choice(TASK_TYPES)
    user = random.choice(USERS)
    title_base = random.choice(TITLES)
    desc_base = random.choice(DESCRIPTIONS)
    
    slug = f"prompt-{index}-{int(time.time())}"
    title = f"{title_base} ({index})"
    description = f"{desc_base} This is prompt #{index}."
    
    payload = {
        "slug": slug,
        "title": title,
        "description": description,
        "taskType": task_type,
        "status": "DRAFT", # Starts as DRAFT
        "authorUsername": user,
        "userId": user
    }
    
    print(f"Creating prompt {index}: {title}")
    try:
        response = requests.post(f"{BASE_URL}/prompt", headers=HEADERS, json=payload)
        response.raise_for_status()
        data = response.json()
        print(f"  Success: {data['payload']['mutatedEntity']['id']}")
        return data['payload']['mutatedEntity']
    except Exception as e:
        print(f"  Error creating prompt: {e}")
        if response:
            print(f"  Response: {response.text}")
        return None

def transition_prompt(prompt, target_state):
    prompt_id = prompt['id']
    print(f"Transitioning prompt {prompt_id} to {target_state}")
    
    # Simple transition logic (simplification of prompt-states.xml)
    # DRAFT -> submit -> OPEN
    # OPEN -> validate -> VALIDATED
    # OPEN -> close -> CLOSED
    
    try:
        if target_state == "OPEN":
            requests.patch(f"{BASE_URL}/prompt/{prompt_id}/submit", headers=HEADERS).raise_for_status()
        elif target_state == "VALIDATED":
            requests.patch(f"{BASE_URL}/prompt/{prompt_id}/submit", headers=HEADERS).raise_for_status()
            requests.patch(f"{BASE_URL}/prompt/{prompt_id}/validate", headers=HEADERS).raise_for_status()
        elif target_state == "CLOSED":
            requests.patch(f"{BASE_URL}/prompt/{prompt_id}/submit", headers=HEADERS).raise_for_status()
            requests.patch(f"{BASE_URL}/prompt/{prompt_id}/close", headers=HEADERS).raise_for_status()
        elif target_state == "BOUNTIED":
             requests.patch(f"{BASE_URL}/prompt/{prompt_id}/submit", headers=HEADERS).raise_for_status()
             requests.patch(f"{BASE_URL}/prompt/{prompt_id}/addBounty", headers=HEADERS, json={"amount": 100}).raise_for_status()
             
    except Exception as e:
        print(f"  Error transitioning: {e}")

def main():
    print("Starting data seeding...")
    prompts = []
    for i in range(1, 51):
        prompt = create_prompt(i)
        if prompt:
            prompts.append(prompt)
    
    print(f"\nCreated {len(prompts)} prompts. Applying random state transitions...")
    
    for prompt in prompts:
        dice = random.random()
        if dice < 0.2:
            # Leave as DRAFT
            continue
        elif dice < 0.5:
            transition_prompt(prompt, "OPEN")
        elif dice < 0.8:
            transition_prompt(prompt, "VALIDATED")
        elif dice < 0.9:
            transition_prompt(prompt, "CLOSED")
        else:
             transition_prompt(prompt, "BOUNTIED")

    print("\nSeeding complete!")

if __name__ == "__main__":
    main()
