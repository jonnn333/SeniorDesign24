%% MFCC
% Voicebox obtained from: http://www.ee.ic.ac.uk/hp/staff/dmb/voicebox/voicebox.html
    [y, Fs] = audioread('audio4.wav');
    N = length(y);
    t = linspace(0, N/Fs, N);
    subplot(2,1,1);
    plot(t,y);
    title('Raw Cough Signal');
    xlabel('Time (s)');
    ylabel('Amplitude (dB)');
    
    subplot(2,1,2);
    [c, tc] = melcepst(y, 44100);
    plot(tc, c);
    title('Mel Frequency Cepstral Coefficients');
    xlabel('Frequency (Hz)');
    ylabel('Mel Coefficients');    

    
%% Server
    t = tcpip('0.0.0.0', 8000, 'NetworkRole', 'Server');
    set(t, 'InputBufferSize', 1000000); 
    t.BytesAvailableFcnMode = 'byte';

% Idle until connection is established
    fprintf('------Capstone S17-24------\n');
    fprintf('Waiting for Client... \n');
    fopen(t);
    fprintf('Client Connected! \n');
    fprintf('Waiting for Data...\n');

timer = 0;
message = [];

while t.status == 'open'
    if timer == 10
        break
    end
    if t.BytesAvailable > 0
        % Brice was here, if this doesn't work it's his fault
        fprintf('THE PROGRAM GOT TO THIS POINT\n')
        % message = fread(t, t.BytesAvailable);
        message = read(t, t.BytesAvailable, 'int16');
        continue
    else
        fprintf('Waiting for Message...\n');
        pause(0.5)
        timer = timer + 1;
        continue
    end
end
%if timer == 20
%    fprintf('Timed out, buster.\n');
%end

fprintf('Message Received!\n');
% please god no, not now    -Brice

filename = 'test.wav';
audiowrite(filename, int16(message), 44100, 'BitsPerSample', 16)
[y, Fs] = audioread(filename);
sound(y, Fs);

L = length(message);
l = linspace(0, L/44100, L);
plot(l, message);

fclose(t);
fprintf('Connection Ended. \n');
% delete(t); 
% clear t 