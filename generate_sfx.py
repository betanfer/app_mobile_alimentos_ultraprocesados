import math
import struct
import wave
import os

SAMPLE_RATE = 44100

def clamp(val, min_val=-1.0, max_val=1.0):
    return max(min_val, min(max_val, val))

def save_wav(filename, samples):
    with wave.open(filename, 'w') as wav_file:
        wav_file.setnchannels(1)  # mono
        wav_file.setsampwidth(2)  # 16-bit
        wav_file.setframerate(SAMPLE_RATE)
        # Convert float samples (-1.0 to 1.0) to 16-bit PCM
        raw_bytes = bytearray()
        for s in samples:
            val = int(clamp(s) * 32767)
            raw_bytes.extend(struct.pack('<h', val))
        wav_file.writeframes(raw_bytes)
    print(f"Generated: {filename} ({len(samples)/SAMPLE_RATE:.2f}s)")

# 1. Bubbly Button Click (Cute bubble pop)
def generate_bubbly():
    duration = 0.30
    num_samples = int(SAMPLE_RATE * duration)
    samples = [0.0] * num_samples
    
    # Primary bubble
    for i in range(num_samples):
        t = i / SAMPLE_RATE
        # Rapid upward chirp: 380 Hz -> 820 Hz
        f = 380 + 440 * (1.0 - math.exp(-t * 35))
        # Decay envelope
        env = math.exp(-t * 18)
        # Smooth attack (first 3ms)
        if t < 0.003:
            env *= (t / 0.003)
        # Sine wave with subtle harmonic
        wave_val = math.sin(2 * math.pi * f * t) + 0.25 * math.sin(4 * math.pi * f * t)
        samples[i] += wave_val * env * 0.75

    # Secondary tiny bubble pop slightly delayed
    delay = int(0.04 * SAMPLE_RATE)
    for i in range(delay, num_samples):
        t = (i - delay) / SAMPLE_RATE
        f = 650 + 400 * (1.0 - math.exp(-t * 40))
        env = math.exp(-t * 24)
        if t < 0.003:
            env *= (t / 0.003)
        wave_val = math.sin(2 * math.pi * f * t)
        samples[i] += wave_val * env * 0.35

    return samples

# 2. Exciting Play Button (Upward powerup arpeggio + shimmer)
def generate_play():
    duration = 1.25
    num_samples = int(SAMPLE_RATE * duration)
    samples = [0.0] * num_samples
    
    # Notes: C5 (523.25), E5 (659.25), G5 (783.99), C6 (1046.5)
    notes = [
        (0.00, 0.12, 523.25),
        (0.10, 0.12, 659.25),
        (0.20, 0.14, 783.99),
        (0.32, 0.90, 1046.50), # sustained root
        (0.32, 0.90, 1318.51), # major 3rd (E6)
        (0.32, 0.90, 1567.98), # 5th (G6)
    ]
    
    for start_t, dur, freq in notes:
        start_idx = int(start_t * SAMPLE_RATE)
        end_idx = min(num_samples, start_idx + int(dur * SAMPLE_RATE))
        for i in range(start_idx, end_idx):
            t = (i - start_idx) / SAMPLE_RATE
            # Attack and decay envelope
            attack = min(1.0, t / 0.015)
            decay = math.exp(-t * (4.5 if dur < 0.2 else 2.5))
            env = attack * decay
            
            # Subtle vibrato on sustained chord
            vib = 1.0 + (0.005 * math.sin(2 * math.pi * 7 * t) if dur > 0.5 else 0)
            
            # Bright chime wave: fundamental + 2nd harmonic + 3rd harmonic
            w = (math.sin(2 * math.pi * freq * vib * t) +
                 0.4 * math.sin(4 * math.pi * freq * vib * t) +
                 0.15 * math.sin(6 * math.pi * freq * vib * t))
            samples[i] += w * env * (0.35 if dur < 0.2 else 0.22)
            
    # Add a gentle rising pitch sweep underneath for extra excitement
    sweep_dur = 0.35
    for i in range(int(sweep_dur * SAMPLE_RATE)):
        t = i / SAMPLE_RATE
        f = 300 + 800 * (t / sweep_dur) ** 2
        env = (t / sweep_dur) * (1.0 - t / sweep_dur) * 0.25
        samples[i] += math.sin(2 * math.pi * f * t) * env

    return samples

# 3. Game Finish Sound (Round completion chime)
def generate_game_finish():
    duration = 1.50
    num_samples = int(SAMPLE_RATE * duration)
    samples = [0.0] * num_samples
    
    # Completion phrase: A5 -> F5 -> G5 -> Final chord (C5 + E5 + G5 + C6)
    notes = [
        (0.00, 0.20, 880.00),  # A5
        (0.18, 0.20, 698.46),  # F5
        (0.36, 0.25, 783.99),  # G5
        # Final resolved bell chord
        (0.55, 0.95, 523.25),  # C5
        (0.55, 0.95, 659.25),  # E5
        (0.55, 0.95, 783.99),  # G5
        (0.55, 0.95, 1046.50), # C6
    ]
    
    for start_t, dur, freq in notes:
        start_idx = int(start_t * SAMPLE_RATE)
        end_idx = min(num_samples, start_idx + int(dur * SAMPLE_RATE))
        for i in range(start_idx, end_idx):
            t = (i - start_idx) / SAMPLE_RATE
            attack = min(1.0, t / 0.01)
            decay = math.exp(-t * (5.0 if dur < 0.3 else 2.8))
            env = attack * decay
            
            # Bell-like chime with slight metallic overtone
            w = (math.sin(2 * math.pi * freq * t) +
                 0.3 * math.sin(2 * math.pi * freq * 2.0 * t) +
                 0.15 * math.sin(2 * math.pi * freq * 2.76 * t))  # inharmonic bell overtone
            samples[i] += w * env * (0.35 if dur < 0.3 else 0.2)

    return samples

# 4. Positive Results (Victory / Fanfare success chime)
def generate_result_positive():
    duration = 1.60
    num_samples = int(SAMPLE_RATE * duration)
    samples = [0.0] * num_samples
    
    # Happy ascending fanfare: C5 -> E5 -> G5 -> B5 -> high C6 triumph
    notes = [
        (0.00, 0.15, 523.25),  # C5
        (0.12, 0.15, 659.25),  # E5
        (0.24, 0.15, 783.99),  # G5
        (0.36, 0.18, 987.77),  # B5
        # Victorious sustained major chord + sparkle
        (0.50, 1.10, 1046.50), # C6
        (0.50, 1.10, 1318.51), # E6
        (0.50, 1.10, 1567.98), # G6
        (0.50, 1.10, 2093.00), # C7 (sparkle)
    ]
    
    for start_t, dur, freq in notes:
        start_idx = int(start_t * SAMPLE_RATE)
        end_idx = min(num_samples, start_idx + int(dur * SAMPLE_RATE))
        for i in range(start_idx, end_idx):
            t = (i - start_idx) / SAMPLE_RATE
            attack = min(1.0, t / 0.012)
            decay = math.exp(-t * (4.0 if dur < 0.2 else 2.2))
            env = attack * decay
            
            # Cheerful, clean tone with warm overtones
            w = (math.sin(2 * math.pi * freq * t) +
                 0.35 * math.sin(2 * math.pi * freq * 2 * t) +
                 0.12 * math.sin(2 * math.pi * freq * 3 * t))
            samples[i] += w * env * (0.32 if dur < 0.2 else 0.18)

    return samples

# 5. Negative Results (Playful gentle womp-womp cartoon failure)
def generate_result_negative():
    duration = 1.60
    num_samples = int(SAMPLE_RATE * duration)
    samples = [0.0] * num_samples
    
    # Descending cartoon womp-womp notes: Eb4 -> D4 -> Db4 -> C4 sliding down
    notes = [
        (0.00, 0.25, 311.13, 0.0),   # Eb4
        (0.28, 0.25, 293.66, 0.0),   # D4
        (0.56, 0.25, 277.18, 0.0),   # Db4
        (0.84, 0.72, 261.63, -40.0), # C4 sliding down by 40Hz with tremolo
    ]
    
    for start_t, dur, base_freq, slide in notes:
        start_idx = int(start_t * SAMPLE_RATE)
        end_idx = min(num_samples, start_idx + int(dur * SAMPLE_RATE))
        for i in range(start_idx, end_idx):
            t = (i - start_idx) / SAMPLE_RATE
            attack = min(1.0, t / 0.02)
            decay = math.exp(-t * (3.5 if dur < 0.3 else 1.8))
            env = attack * decay
            
            # Pitch slide
            curr_f = base_freq + slide * (t / dur)
            # Wah-wah filter effect (tremolo modulation)
            wah = 1.0 + 0.3 * math.sin(2 * math.pi * 9 * t)
            
            # Brass/reed like tone (odd + even harmonics)
            w = (math.sin(2 * math.pi * curr_f * t) +
                 0.5 * math.sin(4 * math.pi * curr_f * t) +
                 0.25 * math.sin(6 * math.pi * curr_f * t) +
                 0.15 * math.sin(8 * math.pi * curr_f * t))
            samples[i] += w * env * wah * 0.35

    return samples

if __name__ == '__main__':
    out_dir = r"app/src/main/res/raw"
    os.makedirs(out_dir, exist_ok=True)
    
    save_wav(os.path.join(out_dir, "sfx_btn_bubbly.wav"), generate_bubbly())
    save_wav(os.path.join(out_dir, "sfx_btn_play.wav"), generate_play())
    save_wav(os.path.join(out_dir, "sfx_game_finish.wav"), generate_game_finish())
    save_wav(os.path.join(out_dir, "sfx_result_positive.wav"), generate_result_positive())
    save_wav(os.path.join(out_dir, "sfx_result_negative.wav"), generate_result_negative())
    print("All sound effects created successfully!")
