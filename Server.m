%% MFCC
    cough = audioread('audio4.wav');
    mfcc = melcepst(cough);
    
%% Server
t = tcpip('0.0.0.0', 8000, 'NetworkRole', 'Server');

fprintf('Waiting for Client... \n');
fopen(t);
fprintf('Client Connected! \n');

messages = 0;
timer = 0;

while t.status == 'open'
    if t.BytesAvailable > 0
        message = fread(t, t.BytesAvailable);
        break
    else
        fprintf('Waiting for Message...\n');
        pause(0.5)
        timer = timer + 1;
        if timer == 1
            break
        end
    end
end

% data = fread(t, t.BytesAvailable);
% plot(data);

% bye = 'Closing Connection';
% fprintf('%s \n', bye);

% fwrite(t, bye);

fprintf('Message Received!\n');
fprintf('%s \n', message);

fclose(t);
fprintf('Connection Ended. \n');
delete(t); 
clear t 